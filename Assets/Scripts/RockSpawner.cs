using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class RockSpawner : MonoBehaviour {

	public GameObject gameManager;
	public GameObject deathTrigger;

	public float difficulty = 0f;

	public float initialDelay = 3f;
	public float minDelay = 0.2f;
	public float maxDelay = 0.0f;

	public float cameraMargin = 1f; //Margin between the spawner and the upper side of the camera viewport
	public float maxRockDiference = 4; //Maximun difference of rock in the columns

	public float difficultyVelocity = 0.02f;
	public float nextRuby = 20f;

	[HideInInspector]
	public float minHeight;

	public GameObject rock;
	public Camera mainCamera;

	private static float rockSize = 1.2f;
	private static int nOfRocks = 7;
	private float initialHeight = 0.6f;
	private float[] heights = new float[nOfRocks];

	public Image DangerBar;

	private ObjectPooler rockPooler; //The pool of rock objects

	private int lastColumn = -1;

	// Use this for initialization
	public void StartRocks () {
		initialize ();

		rockPooler = ObjectPooler.current;

		// Call the Spawn function after de initial delay
		Invoke ("SpawnRock", initialDelay);
	}

	public void initialize(){
		for (int i = 0; i < nOfRocks; i++)
			heights [i] = initialHeight;

		transform.position = new Vector3(transform.position.x, 16.0f, transform.position.z);
		difficulty = 2.0f;
		nextRuby = 20f;
	}

	void Update(){
		if (Time.timeScale != 0.0f) {
			changeDifficulty (difficultyVelocity);
			nextRuby -= difficultyVelocity;
			//Keep the spawner above the camera
			if (transform.position.y < mainCamera.transform.position.y + mainCamera.orthographicSize + cameraMargin)
					transform.position = new Vector3 (transform.position.x, mainCamera.transform.position.y + mainCamera.orthographicSize + cameraMargin, transform.position.z);

			//BARRA
			DangerBar.rectTransform.localScale = new Vector3 (DangerBar.rectTransform.localScale.x, difficulty / 100f, DangerBar.rectTransform.localScale.z);
		}
	}
	
	void SpawnRock(){
		//Column where to spawn the rock
		int col = GetRockColumn();

		//Getting target x and y coordinates for the rock
		float xPos = transform.position.x + (col * rockSize);
		float yPos = heights [col];
		heights [col] = heights [col] + rockSize;

		//Spawning the rock
		CreateRock (xPos, yPos);

		//Calling this method again after a random delay
		CallMethod ();
	}

	void SpawnRockSlide(){
		for (int i = 0; i < nOfRocks; i++) {
			float xPos = transform.position.x + (i * rockSize);
			float yPos = heights [i];

			heights[i] += rockSize;

			CreateRock (xPos, yPos);
		}
		difficulty = 97;
		CallMethod ();

	}

	void SpawnRuby(){
		//Column where to spawn the ruby
		int col = GetRubyColumn();
		
		//Getting target x coordinates for the ruby
		float xPos = transform.position.x + (col * rockSize);
		
		//Spawning the ruby
		CreateRuby (xPos);
		
		//Calling this method again after a random delay
		CallMethod ();
	}

	void CallMethod(){
		if (!deathTrigger.GetComponent<DeathTrigger>().isDead) {
			float delay = getDif () * maxDelay + minDelay;

			if (nextRuby < 0) {
					Invoke ("SpawnRuby", Random.Range (delay, delay + 0.5f));
					nextRuby = RubyBehaviour.help + (50 - difficulty) / 5;
					nextRuby = Random.Range (nextRuby - 3, nextRuby + 5);
			} else if (difficulty >= 100) {
					Invoke ("SpawnRockSlide", Random.Range (delay, delay + 1.5f));
			} else {
					Invoke ("SpawnRock", Random.Range (delay, delay + 0.5f));
			}
		} else {
			gameManager.GetComponent<GameManager>().endScreenUIAppear();
		}
	}

	//Return the column for the next rock to be spawned
	int GetRockColumn(){
		minHeight = heights[0];
		for (int i = 1; i < heights.Length; i++) {
			if(heights[i] < minHeight)
				minHeight = heights[i];
		}

		int column;

		do {
			column = Random.Range (0, nOfRocks);
		} while(heights[column] + rockSize - minHeight > maxRockDiference * rockSize || (column == lastColumn && difficulty >= 85));

		lastColumn = column;
		return column;
	}

	//Return the column for the next ruby to be spawned
	int GetRubyColumn(){
		minHeight = heights[0];
		for (int i = 1; i < heights.Length; i++) {
			if(heights[i] < minHeight)
				minHeight = heights[i];
		}
		
		int column;
		
		do {
			column = Random.Range (0, nOfRocks);
		} while(heights[column] > minHeight + rockSize);
		
		return column;
	}

	//Get the rock from the pool and initialize it
	void CreateRock(float xPos, float yPos){
		GameObject g = rockPooler.GetPooledRock ();
		if (g) {
			g.transform.position = new Vector2 (xPos, Random.Range(transform.position.y - rockSize/2,transform.position.y + rockSize/2));
			g.transform.rotation = transform.rotation;
			g.transform.GetComponent<Rigidbody2D>().isKinematic = false;
			RockCollision rc = g.GetComponent<RockCollision> ();
			rc.targetX = xPos;
			rc.targetHeight = yPos;
			rc.spawner = gameObject;
			g.SetActive (true);
		}
	}

	//Get the rock from the pool and initialize it
	void CreateRuby(float xPos){
		GameObject g = rockPooler.GetPooledRuby ();
		if (g) {
			g.transform.position = new Vector2 (xPos, transform.position.y);
			g.transform.rotation = transform.rotation;
			RubyBehaviour rb = g.GetComponent<RubyBehaviour>();
			rb.spawner = gameObject;
			g.SetActive (true);
		}
	}



	float getDif(){
		return (difficulty / 100 - 1) * -1;
	}

	public void changeDifficulty(float amount){
		if (difficulty < 95 && difficulty + amount >= 95)
			gameManager.GetComponent<GameManager> ().dangerHighText ();

		difficulty += amount;
		if (difficulty > 100)
						difficulty = 100f;
		if (difficulty < 0)
						difficulty = 0.0f;
	
	}

}

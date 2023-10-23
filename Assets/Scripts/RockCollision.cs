using UnityEngine;
using System.Collections;

public class RockCollision : MonoBehaviour {

	[HideInInspector]
	public float targetHeight;
	public ObjectPooler pooler;
	[HideInInspector]
	public GameObject spawner;

	public GameObject audioSource;
 
	public float targetX;

	public bool hasKilled;
	public GameObject blood;

	void Start(){
		audioSource = GameObject.Find("SoundRockEffects");
	}

	// Update is called once per frame
	void Update () {
		if (!transform.GetComponent<Rigidbody2D>().isKinematic && Mathf.Abs (transform.position.y - targetHeight) < 0.1) {
			Collision ();
		}
		if (spawner.GetComponent<RockSpawner>().minHeight - Camera.main.GetComponent<CameraBehavior>().dist - 4.2f > transform.position.y) {
			gameObject.SetActive (false);
		}
	}

	public void HasKilled (float posY){
		hasKilled = true;
		spawnBlood (posY);
		Camera.main.GetComponent<CameraBehavior> ().ChangeTarget (blood.transform);
	}

	void Collision (){
		//Sound
		audioSource.GetComponent<AudioSource> ().Play ();

		//Set rock position
		transform.GetComponent<Rigidbody2D>().isKinematic = true;
		transform.position = new Vector2(targetX, targetHeight);

		//GetComponent<BoxCollider2D> ().size = new Vector2 (1.2f, 1.2f);

		pooler = ObjectPooler.current;

		//Spawn dust collision effects
		SpawnDustEffect (transform.position.x, transform.position.y - 0.6f);
		SpawnDustEffect (transform.position.x + 0.4f, transform.position.y - 0.6f);
		SpawnDustEffect (transform.position.x - 0.4f, transform.position.y - 0.6f);

		if (hasKilled) {
			blood.SetActive(true);
			hasKilled = false;		
		}
	}

	void SpawnDustEffect(float x, float y){
		GameObject effect = pooler.GetPooledDust();
		if (effect) {
			effect.transform.position = new Vector3 (x, y, transform.position.z);
			effect.transform.rotation = transform.rotation;
			effect.SetActive (true);
		}
	}

	public void spawnBlood(float posY){
		pooler = ObjectPooler.current;
		this.blood = pooler.GetPooledBlood();
		if(blood){
			blood.transform.position = new Vector3 (transform.position.x, posY, transform.position.z);
			blood.transform.rotation = transform.rotation;
			blood.transform.parent = transform;

			Transform bloodRender = blood.transform.GetChild(0);
			bloodRender.localPosition = new Vector2(0.0f, -blood.transform.localPosition.y - 0.5f);
			 
		}
	}
}

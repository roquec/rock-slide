using UnityEngine;
using System.Collections;

public class RubyBehaviour : MonoBehaviour {

	public static float help = 15f;

	[HideInInspector]
	public GameObject spawner;

	private bool picked = false;
	private float fadeSpeed = 13.0f;
	private GameObject audioSource, gameManager;

	void Start () {
		GetComponentInChildren<ParticleSystem> ().renderer.sortingLayerName = "Ruby";
		GetComponentInChildren<ParticleSystem> ().renderer.sortingOrder = 0;
		audioSource = GameObject.Find("GemSoundEffects");
		gameManager = GameObject.Find("GameManager");
	}

	void OnTriggerEnter2D(Collider2D coll) {

		if (coll.gameObject.tag == "Player") {

			//Audio
			audioSource.GetComponent<AudioSource> ().Play ();

			picked = true;

			GetComponent<Rigidbody2D>().isKinematic = true;
			GetComponents<BoxCollider2D>()[0].enabled = false;
			GetComponents<BoxCollider2D>()[1].enabled = false;
			transform.GetChild(1).gameObject.SetActive(false);

			gameManager.GetComponent<GameManager>().gemPickedText();
		}

		if (coll.gameObject.tag == "Rock") {
			gameObject.SetActive(false);
			//	Debug.Log ("Ruby aplastado! D:");
		}

	}

	void Update(){
		if (picked) {
			Vector2 target = getTargetPosition();
			transform.position = Vector2.MoveTowards(transform.position, target, fadeSpeed * Time.deltaTime);
			//transform.localScale = Vector2.Lerp(transform.localScale, new Vector2(0.7f,0.7f), fadeSpeed * Time.deltaTime);
			GetComponentInChildren<SpriteRenderer>().color = Color.Lerp(GetComponentInChildren<SpriteRenderer>().color, new Color(255,255,255,0), 3f * Time.deltaTime);

			if(Mathf.Abs(target.x - transform.position.x) < 0.12f && Mathf.Abs(target.y - transform.position.y) < 0.12f){
				picked = false;
				GetComponent<Rigidbody2D>().isKinematic = false;
				GetComponents<BoxCollider2D>()[0].enabled = true;
				GetComponents<BoxCollider2D>()[1].enabled = true;
				transform.GetChild(1).gameObject.SetActive(true);
				GetComponentInChildren<SpriteRenderer>().color = Color.white;

				RockSpawner rs = spawner.GetComponent<RockSpawner>();
				rs.changeDifficulty(-1 * help);

				Deactivate();
			}
		}

	}

	private void Deactivate(){
		gameObject.SetActive(false);
	}

	private Vector2 getTargetPosition(){
		float windowaspect = (float)Screen.width / (float)Screen.height;
		float h = Camera.main.orthographicSize * 2;
		float w = windowaspect * h;
		return new Vector2 (w / 2f - 0.75f, Camera.main.transform.position.y - 0.8f);
	}
}

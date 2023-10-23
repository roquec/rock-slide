using UnityEngine;
using System.Collections;

public class TitleCollision : MonoBehaviour {

	public ObjectPooler pooler;

	public bool hasKilled;
	public GameObject blood;

	public GameObject audioSource;

	public void HasKilled (float posX, float posY){
		hasKilled = true;
		spawnBlood (posX, posY);
		Camera.main.GetComponent<CameraBehavior> ().ChangeTarget (blood.transform);
	}

	void Update () {
		//if (!transform.rigidbody2D.isKinematic && Mathf.Abs (transform.position.y - 1.47f) < 0.1)
			//Collision ();


		if (!transform.GetComponent<Rigidbody2D>().isKinematic && Mathf.Abs (transform.GetComponent<Rigidbody2D>().velocity.y) < 0.1f && transform.position.y < 10f)
			Collision ();
	}

	void Collision (){
		//Sound
		audioSource.GetComponent<AudioSource> ().Play ();

		//Set rock position
		transform.GetComponent<Rigidbody2D>().isKinematic = true;
		
		pooler = ObjectPooler.current;

		//Spawn dust collision effects
		for (int i = 0; i < 13; i++) {
			SpawnDustEffect (-4.2f + 0.7f * i , transform.position.y - Random.Range(1.40f, 1.60f));
		}
		
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

	public void spawnBlood(float posX, float posY){
		pooler = ObjectPooler.current;
		this.blood = pooler.GetPooledBlood();
		if(blood){
			blood.transform.position = new Vector3 (posX, posY, transform.position.z);
			blood.transform.rotation = transform.rotation;
			blood.transform.parent = transform;
			
			Transform bloodRender = blood.transform.GetChild(0);
			bloodRender.localPosition = new Vector2(0.0f, -blood.transform.localPosition.y - 1.3f);
			
		}
	}
}

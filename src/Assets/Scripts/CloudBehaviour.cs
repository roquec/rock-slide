using UnityEngine;
using System.Collections;

public class CloudBehaviour : MonoBehaviour {

	private float vel = 0.2f;

	void OnEnable(){
		vel = Random.Range (0.2f, 0.4f);
		float scale = Random.Range (0.6f, 1.0f);
		transform.localScale = new Vector2 (scale, scale);
	}

	// Update is called once per frame
	void Update () {
	
		transform.position = new Vector2 (transform.position.x + vel * Time.deltaTime, transform.position.y);

		if (transform.position.x > 6.5f)
			gameObject.SetActive (false);
	}
}

using UnityEngine;
using System.Collections;

public class CloudsSpawner : MonoBehaviour {
	
	public Camera mainCamera;

	public float initialDelay = 10f;
	public float minDelay = 5f;
	public float maxDelay = 15f;

	private ObjectPooler pooler;

	// Use this for initialization
	void Start () {
		// Call the Spawn function after de initial delay
		Invoke ("SpawnCloud", initialDelay);

	}
	
	void SpawnCloud(){
		pooler = ObjectPooler.current;
		GameObject g = pooler.GetPooledCloud ();
		if (g) {
			float yPos = Random.Range (mainCamera.transform.position.y - mainCamera.orthographicSize/2, mainCamera.transform.position.y + mainCamera.orthographicSize*2);
			g.transform.position = new Vector2(transform.position.x, yPos);
			g.transform.rotation = transform.rotation;
			g.SetActive(true);
		}

		Invoke ("SpawnCloud", Random.Range (minDelay, maxDelay));
	}
}

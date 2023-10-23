using UnityEngine;
using System.Collections;

public class SidesFollow : MonoBehaviour {

	public Transform target;
	public Camera mainCamera;
	// Update is called once per frame
	void Update () {
		if ((target.position.y + mainCamera.GetComponent<CameraBehavior>().dist + mainCamera.orthographicSize) > (transform.position.y + 15f)) {
			transform.position = new Vector3(transform.position.x, transform.position.y + 10f, transform.position.z);
		}

		if ((target.position.y - mainCamera.GetComponent<CameraBehavior>().dist) < (transform.position.y - 15f)) {
			transform.position = new Vector3(transform.position.x, transform.position.y - 10f, transform.position.z);
		}
	}
}

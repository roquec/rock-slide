using UnityEngine;
using System.Collections;

public class SkyFollow : MonoBehaviour {

	public Transform target;

	public float offset = 10f;

	// Update is called once per frame
	void Update () {
		transform.position = new Vector3 (transform.position.x, target.position.y + offset, transform.position.z);
	}
}

using UnityEngine;
using System.Collections;

public class CameraBehavior : MonoBehaviour {

	public Transform target; //Target followed by the camera, the player usually

	public float targetWidth = 10.8f; //Standard width of the camera viewport
	public float minHeight = 16.2f; //Minimun height of the camera viewport

	public float dist = 4.2f; //Distance between the player and the bottom of the camera

	private float windowaspect;

	public GameObject sideLeft, sideRight, sky;

	void Start(){
		windowaspect = (float)Screen.width / (float)Screen.height;

		adjustResolution ();
	}

	// Update is called once per frame
	void Update () 
	{
		float newAspect = (float)Screen.width / (float)Screen.height;

		if (newAspect != windowaspect) {
			windowaspect = newAspect;
			adjustResolution ();
		}

		if (target) {
			float posY = -4.0f + GetComponent<Camera>().orthographicSize;
			if((target.position.y - dist) > -4.0f)
				posY = target.position.y - dist + GetComponent<Camera>().orthographicSize;

			transform.position = new Vector3 (transform.position.x, posY, transform.position.z);
		}
	}
	
	void adjustResolution(){
		float h = targetWidth / windowaspect;

		if (h >= minHeight)
			GetComponent<Camera>().orthographicSize = h / 2f;
		else
			GetComponent<Camera>().orthographicSize = minHeight / 2f;
	}

	public void ChangeTarget(Transform target){
		this.target = target;
		sideLeft.GetComponent<SidesFollow> ().target = target;
		sideRight.GetComponent<SidesFollow> ().target = target;
		sky.GetComponent<SkyFollow> ().target = target;
	}
	
}

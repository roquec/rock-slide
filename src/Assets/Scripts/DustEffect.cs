using UnityEngine;
using System.Collections;

public class DustEffect : MonoBehaviour {

	// Use this for initialization
	void Start () {
		particleSystem.renderer.sortingLayerName = "Foreground";
	}

	void OnEnable(){
		Invoke ("Destroy", particleSystem.duration);
		particleSystem.enableEmission = true;
	}

	void Destroy(){
		gameObject.SetActive(false);
	}

	void OnDisable(){
		CancelInvoke();
	}
}

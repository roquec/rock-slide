using UnityEngine;
using System.Collections;

public class DustEffect : MonoBehaviour {

	// Use this for initialization
	void Start () {
		GetComponent<ParticleSystem>().GetComponent<Renderer>().sortingLayerName = "Foreground";
	}

	void OnEnable(){
		Invoke ("Destroy", GetComponent<ParticleSystem>().duration);
		GetComponent<ParticleSystem>().enableEmission = true;
	}

	void Destroy(){
		gameObject.SetActive(false);
	}

	void OnDisable(){
		CancelInvoke();
	}
}

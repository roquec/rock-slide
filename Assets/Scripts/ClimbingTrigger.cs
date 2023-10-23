using UnityEngine;
using System.Collections;

public class ClimbingTrigger : MonoBehaviour {

	private IList contacts = new ArrayList();

	void OnTriggerEnter2D(Collider2D coll) {
		if (coll.gameObject.tag == "Rock") {
			if(!contacts.Contains(coll.gameObject)){
				contacts.Add(coll.gameObject);
				updateStatus();
			}
		}
	}
	
	void OnTriggerExit2D(Collider2D coll){
		if (coll.gameObject.tag == "Rock") {
			contacts.Remove(coll.gameObject);
			updateStatus();
		}
	}

	void updateStatus(){
		GetComponentInParent<Movement> ().canClimb = contacts.Count > 0;
	}

	public void initialize(){
		contacts.Clear ();
		updateStatus ();
	}
}

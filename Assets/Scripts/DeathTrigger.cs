using UnityEngine;
using System.Collections;

public class DeathTrigger : MonoBehaviour {

	[HideInInspector]
	public bool isDead = false;

	public GameObject gameManager;

	void OnTriggerEnter2D(Collider2D coll) {
		if (coll.gameObject.tag == "Rock") {
			isDead = true;

			transform.parent.gameObject.SetActive(false);

			RockCollision rc = coll.gameObject.GetComponent<RockCollision>();
			rc.HasKilled(transform.parent.position.y);

			gameManager.GetComponent<GameManager>().endScreen();
		}
		else if(coll.gameObject.tag == "Title") {
			isDead = true;
			
			transform.parent.gameObject.SetActive(false);
			
			TitleCollision tc = coll.gameObject.GetComponent<TitleCollision>();
			tc.HasKilled(transform.parent.position.x, transform.parent.position.y);
		}
	}

	public void initialize(){
		isDead = false;
	}

}

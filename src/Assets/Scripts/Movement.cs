using UnityEngine;
using System.Collections;

public class Movement : MonoBehaviour
{
	public float runSpeed = 5f;
	public float climbSpeed = 3f;

	[HideInInspector]
	public bool canClimb = false;

	private bool climbing = false;
	private bool facingLeft = true;
	private Animator anim;
	private float prevousVerticalVel;

	public int score = 0;
	public GameObject gameManager;

	void Start ()
	{
		anim = GetComponent<Animator> ();
	}

	public void initialize(){
		climbing = false;

		GetComponentInChildren<ClimbingTrigger> ().initialize ();
		GetComponentInChildren<DeathTrigger> ().initialize ();

		score = 0;
	}

	void FixedUpdate ()
	{
		float h = GetInput(); //Get the input

		//Setting the horizontalSpeed
		rigidbody2D.velocity = new Vector2(runSpeed * h, rigidbody2D.velocity.y); 

		//Setting the verticalSpeed
		if (canClimb && ((facingLeft && h < 0) || (!facingLeft && h > 0))) {
			//if (!climbing)
				StartClimbing (); 
		} else {
			if (climbing)
				StopClimbing ();
		}

		//Turning the player if needed
		if (h > 0 && facingLeft)
			Flip ();
		else if(h < 0 && !facingLeft)
			Flip();

		//Setting the variables for the animations
		if (prevousVerticalVel < -10f && rigidbody2D.velocity.y > -1f) {
			anim.SetTrigger ("Landing");
		}
		prevousVerticalVel = rigidbody2D.velocity.y;

		if (getCurrentScore () > score) {
			score = getCurrentScore ();
			if(score == PlayerPrefs.GetInt(GameManager.scorek) + 1 && PlayerPrefs.GetInt(GameManager.scorek) > 0)
				gameManager.GetComponent<GameManager>().newRecordText();
		}
	}

	//Flip the player orientation horizontally
	void Flip(){
		facingLeft = !facingLeft;

		Vector3 theScale = transform.localScale;
		theScale.x *= -1;
		transform.localScale = theScale;
	}

	//Setting the player vertical speed to the climb speed and turning off gravity
	void StartClimbing(){
		climbing = true;

		rigidbody2D.velocity = new Vector2(rigidbody2D.velocity.x, climbSpeed);
		rigidbody2D.gravityScale = 0.0f;
	}

	//Setting the player vertical speed to 0 and turning on gravity
	void StopClimbing(){
		climbing = false;
		
		rigidbody2D.velocity = new Vector2(rigidbody2D.velocity.x, 0.0f);
		rigidbody2D.gravityScale = 1.0f;
	}

	float GetInput(){
		float dir = 0;

		//Keyboard input
		dir = Input.GetAxis("Horizontal");

		//Mouse input
		if (Input.GetMouseButton (0)) {
			if (Camera.main.ScreenToWorldPoint(Input.mousePosition).x > 0 && Input.mousePosition.y < (Screen.height / 2)) 
				dir = 1f;
			else if(Camera.main.ScreenToWorldPoint(Input.mousePosition).x < 0 && Input.mousePosition.y < (Screen.height / 2))
				dir = -1f;
		}

		//Touch input
		if (Input.touchCount > 0)
			if (Camera.main.ScreenToWorldPoint(Input.GetTouch (0).position).x > 0 && Input.GetTouch (0).position.y < (Screen.height / 2))
				dir = 1f;
		else if(Camera.main.ScreenToWorldPoint(Input.GetTouch (0).position).x < 0 && Input.GetTouch (0).position.y < (Screen.height / 2))
				dir = -1f;

		return dir;
	}

	
	private int getCurrentScore(){
		return Mathf.RoundToInt((transform.position.y - 0.6f)/1.2f);
	}
}

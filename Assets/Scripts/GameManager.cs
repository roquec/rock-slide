using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class GameManager : MonoBehaviour {

	public Sprite iconOn, iconOff, pauseOn, pauseOff, soundOn, soundOff;

	private float fadeSpeed = 0.5f;
	public string leaderboard = "CggIwfbA8CQQAhAA";
	public static string scorek = "gasdfjha";
	public static string soundk = "frdgpijnwa";
	public static string logedk = "fjdoijoajdgf";

	ObjectPooler pooler;
	public GameObject player;
	public GameObject rockSpawner;

	public GameObject titleSlide;
	public GameObject titleRock;

	public Button buttonPlay, retryButton, singInOut, leaderboardButton, pauseButton, soundButton;
	public Image DangerBar, DangerBarStyle;
	public Text gameOverText, scoreText, recordText, gameText, signOutText;
	public GameObject screenFader, pauseScreen, scaleCanvas;

	public bool fadeScreen = false;
	public bool uiAppear = false;

	private bool mAuthenticating = false;

	private bool newRecord = false;

	public AudioSource audioMusic, audioRocks, audioGems;
	
	// Use this for initialization
	void Start () {
		setSound ();

		if (!PlayerPrefs.HasKey (logedk))
			PlayerPrefs.SetInt (logedk,1);

		if(PlayerPrefs.GetInt(logedk) == 1)
			Authenticate ();

		titleScreen ();
	}
	
	// Update is called once per frame
	void Update () {
		if(fadeScreen){
			screenFader.GetComponent<Image>().color = Color.Lerp(screenFader.GetComponent<Image>().color, Color.black, fadeSpeed * Time.deltaTime);
			if(screenFader.GetComponent<Image>().color.a > 0.70f && uiAppear)
				ScreenFaded();
		}

		//SALIR CON EL BOTON BACK EN ANDROID
		if (Input.GetKeyDown(KeyCode.Escape)) {	Application.Quit();	}
	}

	public void titleScreen(){
		CleanScreen ();

		//UI
		Invoke ("titleUIAppear", 2.4f);

		//OBJECTS
		titleSlide.transform.position = new Vector2 (0.0f, 20.0f);
		titleSlide.GetComponent<Rigidbody2D>().isKinematic = false;
		titleSlide.SetActive (true);

		titleRock.transform.position = new Vector2 (0.0f, 35.0f);
		titleRock.GetComponent<Rigidbody2D>().isKinematic = false;
		titleRock.SetActive (true);

		Camera.main.GetComponent<CameraBehavior> ().ChangeTarget (player.transform);

		player.SetActive (true);
		player.GetComponent<Movement> ().initialize ();
		player.transform.position = new Vector2 (Random.Range(-3.5f,3.5f), 0.6f);

	}

	private void titleUIAppear(){
		buttonPlay.gameObject.SetActive (true);
	}


	public void gameScreen(){

		CleanScreen ();

		//UI
		scaleCanvas.SetActive (true);
		DangerBar.gameObject.SetActive (true);
		DangerBarStyle.gameObject.SetActive (true);
		pauseButton.gameObject.SetActive (true);
		pauseButton.image.sprite = pauseOn;
		soundButton.gameObject.SetActive (true);

		//OBJECTS
		Camera.main.GetComponent<CameraBehavior> ().ChangeTarget (player.transform);

		//PLAYER
		player.SetActive (true);
		player.GetComponent<Movement> ().initialize ();
		player.transform.position = new Vector2 (Random.Range(-3.5f,3.5f), 0.6f);

		rockSpawner.SetActive(true);
		rockSpawner.GetComponent<RockSpawner> ().StartRocks ();

		//Primera partida
		if (!PlayerPrefs.HasKey (scorek))
			pause ();
	}

	public void pause(){
		if (Time.timeScale > 0.0f) {
			Time.timeScale = 0.0f;
			pauseScreen.SetActive (true);
			audioMusic.Pause();
			pauseButton.image.sprite = pauseOff;
		} else {
			Time.timeScale = 1.0f;
			pauseScreen.SetActive (false);
			audioMusic.Play();
			pauseButton.image.sprite = pauseOn;
		}
	}

	public void endScreen(){
		titleSlide.SetActive (false);
		titleRock.SetActive (false);
		
		buttonPlay.gameObject.SetActive (false);
		DangerBar.gameObject.SetActive (false);
		DangerBarStyle.gameObject.SetActive (false);
		scaleCanvas.SetActive (false);
		
		rockSpawner.SetActive(false);

		screenFader.SetActive (true);
		fadeScreen = true;

		int score = player.GetComponent<Movement> ().score;

		if (score > getMaxScore ()) {
			newRecord = true;
			setMaxScore(score);
		}
		PostToLeaderboard(getMaxScore ());
	}

	private void ScreenFaded(){
		retryButton.gameObject.SetActive (true);
		gameOverText.gameObject.SetActive (true);
		scoreText.gameObject.SetActive (true);
		recordText.gameObject.SetActive (true);
		leaderboardButton.gameObject.SetActive (true);
		fadeScreen = false;
		uiAppear = false;

		scoreText.text = "Score: " + player.GetComponent<Movement> ().score + "m";
		if (newRecord)
			recordText.text = "New record!";
		else
			recordText.text = "Record: " + getMaxScore () + "m";
	}

	public void endScreenUIAppear(){
		uiAppear = true;
	}

	public void CleanScreen(){
		pooler = ObjectPooler.current;
		pooler.deactivateAll ();

		titleSlide.SetActive (false);
		titleRock.SetActive (false);

		gameText.gameObject.SetActive (false);
		signOutText.gameObject.SetActive (false);
		scaleCanvas.SetActive (false);
		pauseScreen.SetActive (false);
		leaderboardButton.gameObject.SetActive (false);
		buttonPlay.gameObject.SetActive (false);
		retryButton.gameObject.SetActive (false);
		DangerBar.gameObject.SetActive (false);
		DangerBarStyle.gameObject.SetActive (false);
		gameOverText.gameObject.SetActive (false);
		scoreText.gameObject.SetActive (false);
		recordText.gameObject.SetActive (false);
		pauseButton.gameObject.SetActive (false);
		soundButton.gameObject.SetActive (false);
		newRecord = false;

		screenFader.SetActive (false);
		screenFader.GetComponent<Image> ().color = Color.clear;

		rockSpawner.SetActive(false);
	}

	public void Authenticate(){
		if (Social.localUser.authenticated || mAuthenticating) {
			return;
		}
		
		// Activate the Play Games platform. This will make it the default
		// implementation of Social.Active
		GooglePlayGames.PlayGamesPlatform.Activate();
		
		// Set the default leaderboard for the leaderboards UI
		((GooglePlayGames.PlayGamesPlatform) Social.Active).SetDefaultLeaderboardForUI(leaderboard);

		// Sign in to Google Play Games
		mAuthenticating = true;
		Social.localUser.Authenticate((bool success) => {
			mAuthenticating = false;
			if (Social.localUser.authenticated) {
				singInOut.image.sprite = iconOn;
				PlayerPrefs.SetInt(logedk,1);
			} else {
				singInOut.image.sprite = iconOff;
				PlayerPrefs.SetInt(logedk,0);
			}
		});
	}

	public void SignOff(){
		if (Social.localUser.authenticated) {
			((GooglePlayGames.PlayGamesPlatform)Social.Active).SignOut ();
			signOutText.gameObject.SetActive(true);
			Invoke("disableSignOutText", 2.1f);
			singInOut.image.sprite = iconOff;
			PlayerPrefs.SetInt(logedk,0);
		}
	}

	private void disableSignOutText(){
		signOutText.gameObject.SetActive (false);
	}

	public void highscores(){
		if (Social.localUser.authenticated)
				Social.ShowLeaderboardUI ();
		else
				Authenticate ();
	}

	public void PostToLeaderboard(int score) {
		if(Social.localUser.authenticated)
			Social.ReportScore(score, leaderboard, (bool success) => {});
	}

	public void SignInOutButton(){
		if (!Social.localUser.authenticated)
			Authenticate ();
		else
			SignOff ();
	}
	
	public int getMaxScore(){
		if(PlayerPrefs.HasKey(scorek)){
			return PlayerPrefs.GetInt(scorek);
		}else{
			PlayerPrefs.SetInt(scorek, 0);
			return 0;
		}
	}

	public void setMaxScore(int score){
		PlayerPrefs.SetInt(scorek, score);
	}

	public void setSound(){
		if (!PlayerPrefs.HasKey (soundk))
			PlayerPrefs.SetInt (soundk, 1);

		if (PlayerPrefs.GetInt (soundk) == 0) {
			audioMusic.mute = true;
			audioRocks.mute = true;
			audioGems.mute = true;
			soundButton.image.sprite = soundOff;
		} else {
			audioMusic.mute = false;
			audioRocks.mute = false;
			audioGems.mute = false;
			soundButton.image.sprite = soundOn;
		}
	}

	public void AudioButton(){
		if (PlayerPrefs.GetInt (soundk) == 1) {
			PlayerPrefs.SetInt (soundk, 0);
		} else {
			PlayerPrefs.SetInt (soundk, 1);
		}
		setSound ();
	}

	//Game messages
	public void newRecordText(){
		if (!gameText.IsActive()) {
			gameText.text = "New record!";
			gameText.color = new Color32(200,200,0,0);
			gameText.gameObject.SetActive(true); 
			Invoke("disableTextEnded", 2.1f);
		}
	}
	public void  gemPickedText(){
		if (!gameText.IsActive()) {
			gameText.text = "Less danger!";
			gameText.color = new Color32(0,200,0,0);
			gameText.gameObject.SetActive(true);
			Invoke("disableTextEnded", 2.1f);
		}
	}
	public void dangerHighText(){
		if (!gameText.IsActive()) {
			gameText.text = "DANGER!";
			gameText.color = new Color32(200,0,0,0);
			gameText.gameObject.SetActive(true);
			Invoke("disableTextEnded", 2.1f);
		}
	}
	private void disableTextEnded(){
		gameText.gameObject.SetActive(false);
	}
}

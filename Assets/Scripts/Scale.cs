using UnityEngine;
using System.Collections;
using UnityEngine.UI;
public class Scale : MonoBehaviour {

	public Camera mainCamera;
	public Image record;

	public void RecordPosition(){
		if (PlayerPrefs.HasKey (GameManager.scorek) && PlayerPrefs.GetInt(GameManager.scorek) > 0) {
			int recordValue = PlayerPrefs.GetInt(GameManager.scorek);

			float posX;
			if(recordValue % 5 == 0)
				posX = 21.1f;
			else
				posX = -33.5f;

			record.transform.localPosition = new Vector2 (posX, (recordValue * 1.2f * 100f) - (transform.position.y * 100f) + 30f); 
			record.gameObject.SetActive (true);
		} else
			record.gameObject.SetActive (false);
	}

	// Update is called once per frame
	void Update () {
		if ((mainCamera.transform.position.y + mainCamera.orthographicSize) > (transform.position.y + 10f)) {
			transform.position = new Vector3(transform.position.x, transform.position.y + 6f, transform.position.z);
			updateTexts();
			RecordPosition();
		}
		
		if ((mainCamera.transform.position.y - mainCamera.orthographicSize) < (transform.position.y - 10f)) {
			transform.position = new Vector3(transform.position.x, transform.position.y - 6f, transform.position.z);
			updateTexts();
			RecordPosition();
		}
	}

	private void updateTexts(){
		Text[] textos = GetComponentsInChildren<Text> ();

		foreach (Text t in textos) {
			t.text = (((t.transform.localPosition.y - 15f)/100 + transform.position.y) / 1.2) + "m";
		}

	}

	void OnEnable() {
		RecordPosition ();
	}
}

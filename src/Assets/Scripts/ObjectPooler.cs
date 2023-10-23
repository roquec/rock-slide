using UnityEngine;
using System.Collections;

public class ObjectPooler : MonoBehaviour {

	public static ObjectPooler current;

	public GameObject dust;
	public int pooledAmountDust;
	GameObject[] pooledDust;

	public GameObject rock;
	public int pooledAmountRock;
	GameObject[] pooledRock;

	public GameObject ruby;
	public int pooledAmountRuby;
	GameObject[] pooledRuby;

	public GameObject blood;
	public int pooledAmountBlood;
	GameObject[] pooledBlood;

	public GameObject cloud;
	public int pooledAmountCloud;
	GameObject[] pooledCloud;

	// Use this for initialization
	void Start () {
		current = this;

		pooledDust = new GameObject[pooledAmountDust];
		pooledRock = new GameObject[pooledAmountRock];
		pooledRuby = new GameObject[pooledAmountRuby];
		pooledBlood = new GameObject[pooledAmountBlood];
		pooledCloud = new GameObject[pooledAmountCloud];

		initialize (pooledDust, dust, pooledAmountDust);
		initialize (pooledRock, rock, pooledAmountRock);
		initialize (pooledRuby, ruby, pooledAmountRuby);
		initialize (pooledBlood, blood, pooledAmountBlood);
		initialize (pooledCloud, cloud, pooledAmountCloud);

	}

	void initialize(GameObject[] array, GameObject gameObject, int amount){
		for (int i = 0; i < amount; i++) {
			GameObject obj = Instantiate(gameObject) as GameObject;
			obj.SetActive(false);
			obj.transform.parent = transform;
			array[i] = obj;
		}
	}
	
	public GameObject GetPooledDust(){
		for (int i = 0; i < pooledAmountDust; i++) {
			if(!pooledDust[i].activeInHierarchy)
				return pooledDust[i];
		}
		return null;
	}

	public GameObject GetPooledRock(){
		for (int i = 0; i < pooledAmountRock; i++) {
			if(!pooledRock[i].activeInHierarchy)
				return pooledRock[i];
		}
		return null;
	}

	public GameObject GetPooledRuby(){
		for (int i = 0; i < pooledAmountRuby; i++) {
			if(!pooledRuby[i].activeInHierarchy)
				return pooledRuby[i];
		}
		return null;
	}

	public GameObject GetPooledBlood(){
		for (int i = 0; i < pooledAmountBlood; i++) {
			if(!pooledBlood[i].activeInHierarchy)
				return pooledBlood[i];
		}
		return null;
	}

	public GameObject GetPooledCloud(){
		for (int i = 0; i < pooledAmountCloud; i++) {
			if(!pooledCloud[i].activeInHierarchy)
				return pooledCloud[i];
		}
		return null;
	}

	public void deactivate(GameObject[] array){
		foreach(GameObject obj in array){
			obj.SetActive(false);
			obj.transform.parent = transform;
		}
	}

	public void deactivateAll(){
		deactivate (pooledDust);
		deactivate (pooledRock);
		deactivate (pooledRuby);
		deactivate (pooledBlood);
		deactivate (pooledCloud);
	}
}

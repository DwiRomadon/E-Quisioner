package volley;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	//Base URL
	private static String base_URL = "http://192.168.43.244:10/";		//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server url POST
	public static String URL = base_URL+"api_equisioner/";
	public static String EDIT_QUIS = "http://192.168.43.244:10/api_equisioner/include/get_quis_byid.php";

	//Server url GET
	public static String VIEW_QUIS=  "http://192.168.43.244:10/api_equisioner/include/view_quis.php?offset";
	//params in API
	public static String TAG = "tag";
	public static String TAG_LOGIN = "login";
	public static String TAG_REGISTER = "regisuser";
	public static String TAG_INPUT_QUIS = "inputquis";
	public static String username = "username";
	public static String email = "email";
	public static String password = "password";
	public static String address = "address";
}
package gaej.beachvolley.server;

import gaej.beachvolley.client.LoginInfo;
import gaej.beachvolley.client.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3301089317177999476L;

	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LoginInfo loginInfo = new LoginInfo();

	    if (user != null) {
	      loginInfo.setLoggedIn(true);
	      loginInfo.setEmailAddress(user.getEmail());
	      loginInfo.setNickname(user.getNickname());
	      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
	    } else {
	      loginInfo.setLoggedIn(false);
	      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return loginInfo;
	}

}

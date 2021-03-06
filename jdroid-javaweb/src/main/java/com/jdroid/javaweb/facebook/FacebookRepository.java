package com.jdroid.javaweb.facebook;

import java.util.List;
import java.util.NoSuchElementException;
import com.jdroid.javaweb.search.PagedResult;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.User;

public class FacebookRepository {
	
	private static String FRIEND_FQL = "SELECT uid,name FROM user WHERE uid in (SELECT uid1 FROM friend WHERE uid2 = me() and uid1 = #friendId#)";
	private static String FRIEND_FQL_REPLACEMENT = "#friendId#";
	private static String FRIENDS_FQL = "SELECT uid,name,is_app_user FROM user WHERE uid in (SELECT uid1 FROM friend WHERE uid2 = me()) order by name";
	private static String FB_ID = "id";
	private static String FB_ME = "me";
	private static String FB_FEED = "/feed";
	private static String FB_SEARCH = "search";
	private static String FB_MESSAGE = "message";
	private static String FB_CAPTION = "caption";
	private static String FB_LINK = "link";
	private static String FB_PICTURE = "picture";
	private static String FB_DESC = "description";
	
	private FacebookClient createFacebookClient(String accessToken) {
		return new DefaultFacebookClient(accessToken);
	}
	
	public Boolean exist(String accessToken, String facebookId) {
		FacebookClient fb = createFacebookClient(accessToken);
		Connection<User> connection = fb.fetchConnection(FB_SEARCH, User.class, Parameter.with(FB_ID, facebookId));
		return !connection.getData().isEmpty();
	}
	
	public User getProfile(String accessToken) {
		FacebookClient fb = createFacebookClient(accessToken);
		return fb.fetchObject(FB_ME, User.class);
	}
	
	public Boolean isFriend(String accessToken, String facebookId) {
		return getFriend(accessToken, facebookId) != null;
	}
	
	public User getFriend(String accessToken, String facebookId) {
		FacebookClient fb = createFacebookClient(accessToken);
		List<User> users = fb.executeQuery(FRIEND_FQL.replaceAll(FRIEND_FQL_REPLACEMENT, facebookId), User.class);
		
		try {
			return users.iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public PagedResult<FqlUser> getFriends(String accessToken) {
		FacebookClient fb = createFacebookClient(accessToken);
		List<FqlUser> users = fb.executeQuery(FRIENDS_FQL, FqlUser.class);
		return new PagedResult<FqlUser>(users, true);
	}
	
	public void publish(String accessToken, String message) {
		publish(accessToken, FB_ME, message);
	}
	
	public void publish(String accessToken, String facebookId, String message) {
		FacebookClient fb = createFacebookClient(accessToken);
		fb.publish(facebookId + FB_FEED, FacebookType.class, Parameter.with(FB_MESSAGE, message));
	}
	
	public void publishLink(String accessToken, String link, String message, String image, String caption,
			String description) {
		publishLink(accessToken, FB_ME, link, message, image, caption, description);
	}
	
	public void publishLink(String accessToken, String facebookId, String link, String message, String image,
			String caption, String description) {
		FacebookClient fb = createFacebookClient(accessToken);
		fb.publish(facebookId + FB_FEED, FacebookType.class, Parameter.with(FB_LINK, link),
			Parameter.with(FB_MESSAGE, message), Parameter.with(FB_PICTURE, image),
			Parameter.with(FB_CAPTION, caption), Parameter.with(FB_DESC, description));
	}
}

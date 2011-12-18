package edu.pennphoto.web;

public class ResponseCode {
	
	private static final String[] messages = {
		"You must log in to continue.",
		"Your new photo has been posted.",
		"An error was encountered while submitting your photo.",
		"Your new circle has been created.",
		"An error was encountered while creating your new circle.",
		"Your new friend has been added.",
		"An error was encountered while adding your new friend.",
		"The photo has been rated.",
		"An error was encountered while rating the photo.",
		"The photo has been tagged.",
		"An error was encountered while tagging the photo."
	};
	
	public static final String PARAMETER_NAME = "msg";
	
	public static final int LOGIN_FAILURE = 0;
	public static final int PHOTO_SUCCESS = 1;
	public static final int PHOTO_ERROR = 2;
	public static final int CIRCLE_SUCCESS = 3;
	public static final int CIRCLE_FAILURE = 4;
	public static final int FRIEND_SUCCESS = 5;
	public static final int FRIEND_FAILURE = 6;
	public static final int RATING_SUCCESS = 7;
	public static final int RATING_FAILURE = 8;
	public static final int TAGGING_SUCCESS = 9;
	public static final int TAGGING_FAILURE = 10;
	
	public static final String getMessage(int code){
		if(code < messages.length){
			return messages[code];
		} else {
			return "Unknown error";
		}
	}
	
	
}

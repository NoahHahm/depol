package com.tacademy.depol.model;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserInfo;

public class Facebook {

	//Request.newUploadPhotoRequest(session, file, callback).executeAsync();
	public static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "read_friendlists");
	private static Facebook instance;
	private StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {
				Request.newMeRequest(session, new GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							Facebook.getInstance().setFacebookLogin(user);
						}
						ServiceAPI.getInstance().RequestSignUp(ApplicationContext.getContext(), session.getAccessToken(), new SimpleServiceListener<UserBasicInfo>(){
							
							@Override
							public void onLoginSuccess(UserBasicInfo data) {
								PropertyManager.getInstance().setMyData(data);
								if (mListener != null) {
									mListener.OnLoginSuccessListener(data);
								}
							}
							
							public void onLoginFail(int statusCode) {
								if (mListener != null) {
									mListener.OnLoginFail(statusCode);
								}
							}
							
							
						});
						
					}
				}).executeAsync();
			}
		}
	};
	
	
	public void requestPhotoUpload(final Activity context, final String url, final String message) {
		
		Session.openActiveSession(context, true, new StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {

					List<String> permissions = session
							.getPermissions();
					if (!isSubsetOf(PERMISSIONS, permissions)) {
						Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
								context, PERMISSIONS);
						session.requestNewPublishPermissions(newPermissionsRequest);
						return;
					}
					
					//Request.newUploadPhotoRequest(session, uri[0], ).executeAsync();
					Bundle params = new Bundle();
					params.putString("url", url);
					params.putString("message", message);					
					
					new Request(
						    session,
						    "/me/photos",
						    params,
						    HttpMethod.POST,
						    new Request.Callback() {
						        public void onCompleted(Response response) {
						           Toast.makeText(context, "페이스북 공유 성공", Toast.LENGTH_SHORT).show();
						        }
						        
						    }
						).executeAsync();
				}
			}
		});
	}
	
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
	
	
	public interface FacebookListener {
		public void OnLoginSuccessListener(UserBasicInfo user);
		public void OnLoginFail(int statusCode);
	}
	
	FacebookListener mListener;
	
	public void setOnLoginSuccessListener(FacebookListener listener) {
		this.mListener = listener;
	}
	
	private Facebook() {

	}
	
	public static Facebook getInstance() {
		if (instance == null) {
			instance = new Facebook();
		}
		return instance;
	}
	
	public void setFacebookLogin (GraphUser user) {
		UserInfo info = getUserData(user);
		PropertyManager.getInstance().setFacebookLogin(true);
		PropertyManager.getInstance().setFacebookId(user.getId());
		PropertyManager.getInstance().setFacebookEmail((String)user.asMap().get("email"));
		
//		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(getUserPicUri(info.getId()).toString());
//		File file = new File(Environment.getExternalStorageDirectory(), UserDataManager.USER_FACEBOOK_IMAGE);
//		UserDataManager.getInstance().saveImage(bitmap, file);		
	}
	
	public void sessionLogin(Activity context) {
		Session.openActiveSession(context, true, statusCallback);        
	}
	
	public void sessionClear() {
		Session session = Session.getActiveSession();
		if (session != null) session.closeAndClearTokenInformation();
	}
	    
    public UserInfo getUserData(GraphUser user) {
    	UserInfo info = new UserInfo();
    	info.setId(user.getId());
    	info.setName(user.getName());    	
    	info.setLink(user.getLink());
    	info.setEmail((String)user.asMap().get("email"));
    	return info;
    }
    
    public Bitmap getUserPic(String userId) {
        String imageURL;
        Bitmap bitmap = null;
        imageURL = "http://graph.facebook.com/"+userId+"/picture?type=large";
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
        } catch (Exception e) {
            Log.d("TAG", "Loading Picture FAILED");
            e.printStackTrace();
        }
        return bitmap;
    }
    
    public Uri getUserPicUri(String userId) {
        String imageURL = "http://graph.facebook.com/"+userId+"/picture?type=large";
        return Uri.parse(imageURL);
    }
    
    public StatusCallback getStatusCallback() {
		return statusCallback;    	
    }
    

    public void sendRequestDialog(final Activity context) {
  
        Session.openActiveSession(context, true, new StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {

					Bundle params = new Bundle();
					params.putString("message", "facebook test message");
					params.putString("method", "apprequests");
					params.putString("title", "Goal Machine");
					params.putString("app_id", context.getString(R.string.app_id));

					WebDialog requestsDialog = (
				            new WebDialog.RequestsDialogBuilder(context,
				                Session.getActiveSession(),
				                params))
				                .setOnCompleteListener(new OnCompleteListener() {

				                    @Override
				                    public void onComplete(Bundle values,
				                        FacebookException error) {
				                        if (error != null) {
				                            if (error instanceof FacebookOperationCanceledException) {
				                                Toast.makeText(context, 
				                                    "Request cancelled", 
				                                    Toast.LENGTH_SHORT).show();
				                            } else {
				                                Toast.makeText(context, 
				                                    "Network Error", 
				                                    Toast.LENGTH_SHORT).show();
				                            }
				                        } else {
				                            final String requestId = values.getString("request");
				                            if (requestId != null) {
				                                Toast.makeText(context, 
				                                    "Request sent",  
				                                    Toast.LENGTH_SHORT).show();
				                            } else {
				                                Toast.makeText(context, 
				                                    "Request cancelled", 
				                                    Toast.LENGTH_SHORT).show();
				                            }
				                        }   
				                    }

				                })
				                .build();
					
				        requestsDialog.show();
				}
			}
		});
    }
    
    
    // facebook에 포스팅
    public void publishStory(final Activity context) {
        Session session = Session.getActiveSession();
        if (session != null) {
             
            // Check for publish permissions    
			List<String> permissions = session
					.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						context, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
 
            Bundle postParams = new Bundle();
            postParams.putString("name", "몽키3 추천음악");
            postParams.putString("caption", "-girls day-");
            postParams.putString("description", "걸스데이랑께");
            postParams.putString("link", "http://www.monkey3.co.kr/#/etc.album&albumID=185582");
            postParams.putString("picture", "http://imgtest.monkey3.co.kr/get_image.php?type=album&id=185582&w=100");
 
            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                        Toast.makeText(
                        		context,
                                "등록성공",
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        //Log.i(TAG, "JSON error "+ e.getMessage());
                    }
                     
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, postId, Toast.LENGTH_LONG).show();
                    }
                }
            };
 
            Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
    }

}

package com.tacademy.depol.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.data.Account;
import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.ImageUploadData;
import com.tacademy.depol.data.LikeInfo;
import com.tacademy.depol.data.LikeItem;
import com.tacademy.depol.data.MenuNotiInfo;
import com.tacademy.depol.data.MessageInfo;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.data.NoticeItem;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.PortfolioListItem;
import com.tacademy.depol.data.Result;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserItem;
import com.tacademy.depol.data.ViewerDataInfo;

public class ServiceAPI {
	
	//http://testcom.hoantas.cloulu.com
	//http://depol.cafe24app.com
	public static final int PK_ERROR_CODE = 1001;
	
	public static final String DOMAIM = "http://dbgtdbz2.dothome.co.kr/depol/";
	
	public static final String IMAGE_URL = DOMAIM + "Portfolio/Viewer.php";
	public static final String MAIN_URL = DOMAIM + "Portfolio/MainList.php";	
	public static final String LOGIN_URL = DOMAIM + "Login.php";
	public static final String USER_INFO_URL = DOMAIM + "MyPage/User_info.php";
	public static final String FACEBOOK_REGEDIT_URL = DOMAIM + "FacebookLogin.php";
	public static final String UPDATE_TEST_URL = DOMAIM + "Portfolio/Modify.php";
	public static final String UPLOAD_TEST_URL = DOMAIM + "Portfolio/UploadTest.php";
	public static final String COMMENT_URL = DOMAIM + "Portfolio/Comment_Viewer.php";
	public static final String COMMENT_WRITE_URL = DOMAIM + "Portfolio/Comment_Write.php";
	public static final String COMMENT_REMOVE_URL = DOMAIM + "Portfolio/Comment_Remove.php";
	public static final String LIKE_URL = DOMAIM + "Portfolio/Like.php";
	public static final String UNLIKE_URL = DOMAIM + "Portfolio/UnLike.php";
	public static final String FOLLOW_URL = DOMAIM + "Follow/Follow.php";
	public static final String FOLLOW_UN_URL = DOMAIM + "Follow/UnFollow.php";
	public static final String FOLLOW_LIST_URL = DOMAIM + "Follow/FollowerList.php";
	public static final String FOLLOWING_LIST_URL = DOMAIM + "Follow/FollowingList.php";
	public static final String REMOVE_TEST_URL = DOMAIM + "Portfolio/Remove.php";
	public static final String SEARCH_URL = DOMAIM + "Portfolio/Search.php";
	public static final String MESSAGE_INBOX_LIST_URL = DOMAIM + "Message/inboxList.php";
	public static final String MESSAGE_OUTBOX_LIST_URL = DOMAIM + "Message/outboxList.php";
	public static final String MESSAGE_READ_URL = DOMAIM + "Message/message_read.php";
	public static final String MESSAGE_WRITE_URL = DOMAIM + "Message/message_write.php";
	public static final String MESSAGE_REMOVE_URL = DOMAIM + "Message/message_remove.php";
	public static final String LIKE_LIST_URL = DOMAIM + "Like/like_history.php";
	public static final String LIKE_READ_URL = DOMAIM + "Like/like_read.php";
	public static final String INFO_BASIC_MODIFY_URL = DOMAIM + "MyPage/User_Basic_Modify.php";
	public static final String INFO_EDUCATION_MODIFY_URL = DOMAIM + "MyPage/User_Education_Modify.php";
	public static final String INFO_CERTIFICATION_MODIFY_URL = DOMAIM + "MyPage/User_Certification_Modify.php";	
	public static final String INFO_WORK_MODIFY_URL = DOMAIM + "MyPage/User_Career_Modify.php";	
	public static final String INFO_AWARD_MODIFY_URL = DOMAIM + "MyPage/User_Award_Modify.php";	
	public static final String INFO_INFOGRAPHICS_MODIFY_URL = DOMAIM + "MyPage/User_Ability_Modify.php";
	public static final String REGEDIT_URL = DOMAIM + "Register.php";
	public static final String NOTICE_URL = DOMAIM + "Notice.php";	
	public static final String PROFILE_PIC_UPLOAD_URL = DOMAIM + "MyPage/Upload_Profile_Image.php";	
	public static final String UPDATE_PROFILE_URL = DOMAIM + "MyPage/Update_Profile.php";
	public static final String VERSION_URL = DOMAIM + "Version.php";	
	public static final String PORTFOLIO_LIKE_USER_URL = DOMAIM + "Like/like_user_history.php";
	
	public static final String MENU_NOTI_URL = DOMAIM + "view/menu/noti_num";
	public static final String EMAIL_MODIFY_URL = DOMAIM + "settings/email_modity.php";
	public static final String PASSWORD_MODIFY_URL = DOMAIM + "settings/password_modity.php";
	
	

	public static final String FILE_UPLOAD_URL = DOMAIM + "modify/insert_pofol"; //사용X

	
	
	private PersistentCookieStore mCookieStore;
	private AsyncHttpClient client;
	
	private static ServiceAPI instance;
	
	private ServiceAPI() {
		client = new AsyncHttpClient();
		client.setURLEncodingEnabled(false);
		mCookieStore = new PersistentCookieStore(ApplicationContext.getContext());
		client.setCookieStore(mCookieStore);
	}
	
	public static ServiceAPI getInstance() {
		if (instance == null) {
			instance = new ServiceAPI();
		}
		return instance;
	}
	

	
	
    public void RequestNewPortfolio(Context context, int tag, int pageNum, final SimpleServiceListener<PortfolioListItem> listener) {
    	RequestParams params = new RequestParams();
    	params.put("TAG", String.format("%d", tag));
    	params.put("PAGE_NUM", "0");
    	//params.put("TIME_STAMP", "Tue Feb 10 2014 10:16:59 GMT+0900");
    	
    	client.get(context, MAIN_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				PortfolioListItem items = gson.fromJson(new String(responseBody), PortfolioListItem.class);
				
				if (items.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onServiceSuccessListener(items);
					}
				} else {
					if (listener != null) {
						listener.onServiceFailListener(statusCode);
					}
				}

				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onServiceFailListener(statusCode);
				}
				
			}
		});
    }
    	
	public void RequestPortfolio(Context context, final int id, final SimpleServiceListener<PortfolioItem> listener) {
		if (id < 1) {
			if (listener != null) {
				listener.onServiceFailListener(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", id));
		
		client.get(context, IMAGE_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Gson gson = new Gson();
				
				
				PortfolioItem item = gson.fromJson(new String(responseBody),
						PortfolioItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onServiceSuccessListener(item);
					}
				} else {
					if (listener != null) {
						listener.onServiceFailListener(statusCode);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onServiceFailListener(statusCode);
				}
			}

		});
    }
	
	public void RequestLogin(Context context, final String id, String password, final SimpleServiceListener<UserBasicInfo> listener) {
		RequestParams params = new RequestParams();
		params.put("email", id);
		params.put("password", password);
		
		client.post(context, LOGIN_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
								
				Gson gson = new Gson();
				UserBasicInfo item = gson.fromJson(new String(responseBody), UserBasicInfo.class);
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onLoginSuccess(item);
					}
				} else if (item.result.equals("LOGIN_PASSWORD_FAIL")) {
					if (listener != null) {
						listener.onLoginPasswordFail(statusCode);
					}
				} else if (item.result.equals("EMAIL_NONEXISTENT")) {
					if (listener != null) {
						listener.onLoginIdFail(statusCode);
					}
				} else {
					if (listener != null) {
						listener.onLoginFail(statusCode);
					}
				}
				
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onLoginFail(statusCode);
				}
			}

		});

    }
	

	public void RequestUserProfile(Context context, int id, final SimpleServiceListener<UserItem> listener) {
		if (id < 1) {
			if (listener != null) {
				listener.onServiceFailListener(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", id));
		client.get(context, USER_INFO_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onServiceSuccessListener(item);
					}
				} else {
					if (listener != null) {
						listener.onServiceFailListener(statusCode);
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onServiceFailListener(statusCode);
				}
			}
		});
    }
	
	public void RequestFileUpload(Context context, ArrayList<MultplePhotoItem> fileList, final SimpleServiceListener listener) {
	    
		RequestParams params = new RequestParams();
		params.put("SESSION_ID", "ASASD@");
		params.put("POPOL_TITLE", "테스트1");
		params.put("POPOL_TEXT", "바람의나라");

		String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
		File[] file = new File[fileList.size()];
		for(int i=0;i<fileList.size();i++) {

			Cursor cursor = ApplicationContext.getContext().getContentResolver().query(Uri.parse(fileList.get(i).path), proj, null, null, null);
			if (cursor.moveToNext()) { //초기는 -1 주의
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				String dataPath = cursor.getString(column_index);
				file[i] = new File(dataPath.toString());
			}			
						
		}
		try {
			params.put("POPOL_PIC", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("CATEGORY", new String[] {"가", "나"});
		
		client.post(context, FILE_UPLOAD_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFileUploadSuccess();
					}
				} else {
					if (listener != null) {
						listener.onFileUploadFail(statusCode);
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onFileUploadFail(statusCode);
				}
			}
			
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
				double progress = (double)(bytesWritten*100)/totalSize;
			}
		});
		
	}
	

	public void RequestSignUp(Context context, String email, String password, String name, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("email", email);
		params.put("name", name);
		params.put("password", password);
		
		client.post(context, REGEDIT_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onSiginUpSuccess();
					}
				} else {
					if (listener != null) {
						listener.onSiginUpFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onSiginUpFail(statusCode);
				}
			}
		});
		
	}
	
	public void RequestSignUp(Context context, String accessToken, final SimpleServiceListener<UserBasicInfo> listener) {
		RequestParams params = new RequestParams();
		params.put("access_token", accessToken);
		
		client.post(context, FACEBOOK_REGEDIT_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				UserBasicInfo item = gson.fromJson(new String(responseBody), UserBasicInfo.class);
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onLoginSuccess(item);
					}
				} else {
					if (listener != null) {
						listener.onLoginFail(statusCode);
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onLoginFail(statusCode);
				}
			}
		});		
	}
	
	public void RequestBasicInfoModify(Context context, BasicInfo info, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("name", info.userName);
		params.put("position", info.userPosition);
		params.put("location", info.location);
		params.put("email", info.email);
		params.put("birth", info.birth);
		params.put("website", info.website);
		
		client.post(context, INFO_BASIC_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onBasicInfoModifySuccess();
					}					
				} else {
					if (listener != null) {
						listener.onBasicInfoModifyFail(statusCode);
					}					
				}

			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onBasicInfoModifyFail(statusCode);
				}
			}
		});		
	}

	public void RequestEducationInfoModify(Context context,String json,  final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("json", json);
		
		client.post(context, INFO_EDUCATION_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onEducationInfoModifySuccess(item);
					}					
				} else {
					if (listener != null) {
						listener.onEducationInfoModifyFail(statusCode);
					}					
				}

			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onEducationInfoModifyFail(statusCode);
				}
			}
		});		
	}
	
	public void RequestWorkInfoModify(Context context, String json,  final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();
		params.put("json", json);
		
		client.post(context, INFO_WORK_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onWorkInfoModifySuccess(item);
					}					
				} else {
					if (listener != null) {
						listener.onWorkInfoModifyFail(statusCode);
					}					
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onWorkInfoModifyFail(statusCode);
				}
			}
		});		
	}

	
	public void RequestCertificationInfoModify(Context context, String json, final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();
		params.put("json", json);
		
		client.post(context, INFO_CERTIFICATION_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onCertificationInfoModifySuccess(item);
					}					
				} else {
					if (listener != null) {
						listener.onCertificationInfoModifyFail(statusCode);
					}					
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onCertificationInfoModifyFail(statusCode);
				}
			}
		});		
	}
	

	public void RequestAwordInfoModify(Context context,String json, final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();
		params.put("json", json);
		
		client.post(context, INFO_AWARD_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onAwardInfoModifySuccess(item);
					}					
				} else {
					if (listener != null) {
						listener.onAwardInfoModifyFail(statusCode);
					}					
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onAwardInfoModifyFail(statusCode);
				}
			}
		});		
	}
	

	public void RequestAbilityInfoModify(Context context, String json, final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();
		params.put("json", json);
		
		client.post(context, INFO_INFOGRAPHICS_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onAbilityInfoModifySuccess(item);
					}					
				} else {
					if (listener != null) {
						listener.onAbilityInfoModifyFail(statusCode);
					}					
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (listener != null) {
					listener.onAbilityInfoModifyFail(statusCode);
				}
			}
		});		
	}
	
	
	

	public void RequestFileUploadTest(Context context, ImageUploadData imageUploadDto, File[] file, final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();
		params.put("title", imageUploadDto.pofolTitle);
		params.put("context", imageUploadDto.pofolText);
		String[] category = new String[imageUploadDto.category.length];
		for(int i=0;i<imageUploadDto.category.length;i++) {
			category[i] = String.format("%d", imageUploadDto.category[i]);
		}
		params.put("category", category);
				
		try {
			params.put("images[]", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		client.post(context, UPLOAD_TEST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFileUploadSuccess();
					}
				} else {

					if (listener != null) {
						listener.onFileUploadFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onFileUploadFail(statusCode);
				}
			}
			
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				if (listener != null) {
					listener.onProgress(bytesWritten, totalSize);
				}
			}
		});		
	}
	

	public void RequestRemoveTest(Context context, int id, final SimpleServiceListener<UserItem> listener) {
		if (id < 1) {
			if (listener != null) {
				listener.onPortfolioRemoveFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", id));

		client.get(context, REMOVE_TEST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioRemoveSuccess();
					}
				} else {

					if (listener != null) {
						listener.onPortfolioRemoveFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onPortfolioRemoveFail(statusCode);
				}
			}
			
		});		
	}
	

	public void RequestUpdateTest(Context context, ImageUploadData imageUploadDto, File[] file, final SimpleServiceListener<UserItem> listener) {
		RequestParams params = new RequestParams();

		params.put("id", String.format("%d", imageUploadDto.imgId));
		params.put("title", imageUploadDto.pofolTitle);
		params.put("context", imageUploadDto.pofolText);
		String[] category = new String[imageUploadDto.category.length];
		for(int i=0;i<imageUploadDto.category.length;i++) {
			category[i] = String.format("%d", imageUploadDto.category[i]);
		}
		params.put("category", category);
				
		try {
			params.put("images[]", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		client.post(context, UPDATE_TEST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				Result item = gson.fromJson(new String(responseBody), Result.class);

				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioUpdateSuccess();
					}
				} else {
					if (listener != null) {
						listener.onPortfolioUpdateFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onPortfolioUpdateFail(statusCode);
				}
			}
			

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				if (listener != null) {
					listener.onProgress(bytesWritten, totalSize);
				}
			}
		});		
	}
	
	

	public void RequestUpdateProfile(Context context, final SimpleServiceListener<UserBasicInfo> listener) {
		RequestParams params = new RequestParams();
		UserBasicInfo info = PropertyManager.getInstance().getMyData();
		params.put("name", info.userName);
		params.put("status", String.format("%d", info.userRecruitStatus));
		params.put("position", info.userPosition);

		client.get(context, UPDATE_PROFILE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserBasicInfo item = gson.fromJson(new String(responseBody),
						UserBasicInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					PropertyManager.getInstance().setMyData(item);
					if (listener != null) {
						listener.onProfileUpdateSuccess(item);
					}
				} else {

					if (listener != null) {
						listener.onProfileUpdateFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onProfileUpdateFail(statusCode);
				}
			}
			
		});		
	}
	

	public void RequestComment(Context context,int id, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (id < 1) {
			if (listener != null) {
				listener.onPortfolioCommentRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", id));

		client.get(context, COMMENT_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				ViewerDataInfo item = gson.fromJson(new String(responseBody),
						ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioCommentRequestSuccess(item.data);
					}
				} else {

					if (listener != null) {
						listener.onPortfolioCommentRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				
				if (listener != null) {
					listener.onPortfolioCommentRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestCommentWrite(Context context,int portid, int targetpk, String text, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (portid < 1 || targetpk < 1) {
			if (listener != null) {
				listener.onPortfolioCommentWriteRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", portid));
		params.put("targer_user", String.format("%d", targetpk));
		params.put("context", text);

		client.post(context, COMMENT_WRITE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				ViewerDataInfo item = gson.fromJson(new String(responseBody),
						ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioCommentWriteRequestSuccess(item.data);
					}
				} else {

					if (listener != null) {
						listener.onPortfolioCommentWriteRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				
				if (listener != null) {
					listener.onPortfolioCommentWriteRequestFail(statusCode);
				}
				
			}
			
		});		
	}


	public void RequestCommentRemove(Context context,int commentid, int portpk, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (commentid < 1 || portpk < 1) {
			if (listener != null) {
				listener.onPortfolioCommentRemoveRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("comment_id", String.format("%d", commentid));
		params.put("id", String.format("%d", portpk));

		client.get(context, COMMENT_REMOVE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				ViewerDataInfo item = gson.fromJson(new String(responseBody), ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioCommentRemoveRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onPortfolioCommentRemoveRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onPortfolioCommentRemoveRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestPortfolioLike(Context context,int userId, int portfolioId, final SimpleServiceListener listener) {
		if (userId < 1 || portfolioId < 1) {
			if (listener != null) {
				listener.onPortfolioLikeRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("pid", String.format("%d", portfolioId));

		client.get(context, LIKE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioLikeRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onPortfolioLikeRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onPortfolioLikeRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestPortfolioUnLike(Context context,int userId, int portfolioId, final SimpleServiceListener listener) {
		if (userId < 1 || portfolioId < 1) {
			if (listener != null) {
				listener.onPortfolioUnLikeRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("pid", String.format("%d", portfolioId));

		client.get(context, UNLIKE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioUnLikeRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onPortfolioUnLikeRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onPortfolioUnLikeRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestPortfolioLikeUser(Context context, int portfolioId, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (portfolioId < 1) {
			if (listener != null) {
				listener.onPortfolioLikeUserRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("pid", String.format("%d", portfolioId));

		client.get(context, PORTFOLIO_LIKE_USER_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				ViewerDataInfo item = gson.fromJson(new String(responseBody),
						ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPortfolioLikeUserRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onPortfolioLikeUserRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onPortfolioLikeUserRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestFollow(Context context, int userId, final SimpleServiceListener listener) {
		if (userId < 1) {
			if (listener != null) {
				listener.onFollowRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("fid", String.format("%d", userId));

		client.get(context, FOLLOW_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFollowRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onFollowRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onFollowRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestUnFollow(Context context, int userId, final SimpleServiceListener listener) {
		if (userId < 1) {
			if (listener != null) {
				listener.onPortfolioUnLikeRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("fid", String.format("%d", userId));

		client.get(context, FOLLOW_UN_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onUnFollowRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onUnFollowRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onUnFollowRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestFollowList(Context context, int userId, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (userId < 1) {
			if (listener != null) {
				listener.onFollowListRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", userId));

		client.get(context, FOLLOW_LIST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				ViewerDataInfo item = gson.fromJson(new String(responseBody),
						ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFollowListRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onFollowListRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onFollowListRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	
	

	public void RequestFollowingList(Context context, int userId, final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		if (userId < 1) {
			if (listener != null) {
				listener.onFollowIngListRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("id", String.format("%d", userId));

		client.get(context, FOLLOWING_LIST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				ViewerDataInfo item = gson.fromJson(new String(responseBody),
						ViewerDataInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFollowIngListRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onFollowIngListRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onFollowIngListRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestMessageInBoxList(Context context, final SimpleServiceListener<ArrayList<MessageItem>> listener) {
		
		RequestParams params = new RequestParams();

		client.post(context, MESSAGE_INBOX_LIST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				MessageInfo item = gson.fromJson(new String(responseBody),
						MessageInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMessageListRequestSuccess(item.message);
					}
				} else {
					if (listener != null) {
						listener.onMessageListRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onMessageListRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestMessageOutBoxList(Context context, final SimpleServiceListener<ArrayList<MessageItem>> listener) {
		
		RequestParams params = new RequestParams();

		client.post(context, MESSAGE_OUTBOX_LIST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				MessageInfo item = gson.fromJson(new String(responseBody),
						MessageInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMessageListRequestSuccess(item.message);
					}
				} else {
					if (listener != null) {
						listener.onMessageListRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onMessageListRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestMessageRead(Context context, int messageId, final SimpleServiceListener<ArrayList<MessageItem>> listener) {

		if (messageId < 1) {
			if (listener != null) {
				listener.onMessageReadRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("mid", String.format("%d", messageId));

		client.get(context, MESSAGE_READ_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				MessageInfo item = gson.fromJson(new String(responseBody),
						MessageInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMessageReadRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onMessageReadRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onMessageReadRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	
	public void RequestMessageSend(Context context, int userId, String message, final SimpleServiceListener<ArrayList<MessageItem>> listener) {

		if (userId < 1) {
			if (listener != null) {
				listener.onMessageSendRequestFail(PK_ERROR_CODE);
			}			
		}
		RequestParams params = new RequestParams();
		params.put("receive_user_id", String.format("%d", userId));
		params.put("context", message);

		client.post(context, MESSAGE_WRITE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				MessageInfo item = gson.fromJson(new String(responseBody),
						MessageInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMessageSendRequestSuccess();
					}
				} else {
					if (listener != null) {
						listener.onMessageSendRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onMessageSendRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestLikeList(Context context, final SimpleServiceListener<ArrayList<LikeItem>> listener) {
		RequestParams params = new RequestParams();
		client.get(context, LIKE_LIST_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				LikeInfo item = gson.fromJson(new String(responseBody),
						LikeInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onLikeListRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onLikeListRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onLikeListRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestLikeRead(Context context, int likeId, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("lid", String.format("%d", likeId));
		
		client.get(context, LIKE_READ_URL, params, new AsyncHttpResponseHandler() {
						
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onLikeReadRequestSuccess(item.likeId);
					}
				} else {
					if (listener != null) {
						listener.onLikeReadRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onLikeReadRequestFail(statusCode);
				}
				
			}
			
		});		
	}

	public void RequestMessageRemove(Context context, int messageId, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("mid", String.format("%d", messageId));
		
		client.get(context, MESSAGE_REMOVE_URL, params, new AsyncHttpResponseHandler() {
						
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				
				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody),
						Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMessageRemoveRequestSuccess(0);
					}
				} else {
					if (listener != null) {
						listener.onMessageRemoveRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
				if (listener != null) {
					listener.onMessageRemoveRequestFail(statusCode);
				}
				
			}
			
		});		
	}
	

	public void RequestProfilePicUpload(Context context, File file, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		try {
			params.put("images", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		client.post(context, PROFILE_PIC_UPLOAD_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				UserItem item = gson.fromJson(new String(responseBody),
						UserItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onFileUploadSuccess();
					}
				} else {
					if (listener != null) {
						listener.onFileUploadFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onFileUploadFail(statusCode);
				}
			}
			
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				if (listener != null) {
					listener.onProgress(bytesWritten, totalSize);
				}
			}
		});		
	}
	

	public void RequestSearch(Context context, String keyword, String[] category , final SimpleServiceListener<ArrayList<PortfolioItem>> listener) {
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		params.put("category", category);
		client.get(context, SEARCH_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				
				PortfolioListItem item = gson.fromJson(new String(responseBody),
						PortfolioListItem.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onSearchRequestSuccess(item.pofol);
					}
				} else {
					if (listener != null) {
						listener.onSearchRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onSearchRequestFail(statusCode);
				}
			}
		});		
	}

	public void RequestNoti(Context context, final SimpleServiceListener<MenuNotiInfo> listener) {
		RequestParams params = new RequestParams();
		client.post(context, MENU_NOTI_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				MenuNotiInfo item = gson.fromJson(new String(responseBody), MenuNotiInfo.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onMenuNotiRequestSuccess(item);
					}
				} else {
					if (listener != null) {
						listener.onMenuNotiRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onMenuNotiRequestFail(statusCode);
				}
			}
		});		
	}
	

	public void RequestEmailModify(Context context, String email, final SimpleServiceListener<Account> listener) {
		RequestParams params = new RequestParams();
		params.put("email", email);
		client.get(context, EMAIL_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				Account item = gson.fromJson(new String(responseBody), Account.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onEmailModifyRequestSuccess(item);
					}
				} else if (item.result.equals("EMAIL_DUPLICATION_FAIL")) {
					if (listener != null) {
						listener.onEmailModifyRequestFail(statusCode);
					}
				} else {
					if (listener != null) {
						listener.onEmailModifyRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onEmailModifyRequestFail(statusCode);
				}
			}
		});		
	}
	

	public void RequestPasswordModify(Context context, String oldPass, String newPass, final SimpleServiceListener listener) {
		RequestParams params = new RequestParams();
		params.put("bepass", oldPass);
		params.put("afpass", newPass);
		client.post(context, PASSWORD_MODIFY_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				
				Gson gson = new Gson();
				Account item = gson.fromJson(new String(responseBody), Account.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onPasswordModifyRequestSuccess();
					}
				} else if (item.result.equals("REQUEST_PASSWORD_FAIL")) {
					if (listener != null) {
						listener.onPasswordModifyRequestOldPassFail();
					}
				} else {
					if (listener != null) {
						listener.onPasswordModifyRequestFail(statusCode);
					}
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onPasswordModifyRequestFail(statusCode);
				}
			}
		});		
	}	
	

	public void RequestNotice(Context context, final SimpleServiceListener<ArrayList<NoticeItem>> listener) {
		RequestParams params = new RequestParams();
		client.get(context, NOTICE_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Gson gson = new Gson();
				
				Type collectionType = new TypeToken<Result<NoticeItem>>() {}.getType();
				Result<NoticeItem> item = new Result<NoticeItem>();
				item = gson.fromJson(new String(responseBody), collectionType);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onNoticeRequestSuccess(item.data);
					}
				} else {
					if (listener != null) {
						listener.onNoticeRequestFail(statusCode);
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onNoticeRequestFail(statusCode);
				}
			}
		});		
	}	
	
	

	public void RequestVersion(Context context, final SimpleServiceListener<String> listener) {
		RequestParams params = new RequestParams();
		client.get(context, VERSION_URL, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				Gson gson = new Gson();
				
				Result item = gson.fromJson(new String(responseBody), Result.class);
				
				if (item.result.equals("SUCCESS")) {
					if (listener != null) {
						listener.onVersionRequestSuccess(item.version);
					}
				} else {
					if (listener != null) {
						listener.onVersionRequestFail(statusCode);
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (listener != null) {
					listener.onVersionRequestFail(statusCode);
				}
			}
		});		
	}	
	
	
	public void cancelNetwork(Context context) {
		client.cancelRequests(context, true);
	}
	
}

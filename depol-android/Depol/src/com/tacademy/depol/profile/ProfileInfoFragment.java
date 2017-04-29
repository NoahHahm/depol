package com.tacademy.depol.profile;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.tacademy.depol.R;
import com.tacademy.depol.data.AbilityInfo;
import com.tacademy.depol.data.AwordInfo;
import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.CertificationInfo;
import com.tacademy.depol.data.DateInfo;
import com.tacademy.depol.data.JsonData;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserItem;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.dynamicview.AcademicDialogFragment;
import com.tacademy.depol.profile.dynamicview.AwordDialogFragment;
import com.tacademy.depol.profile.dynamicview.CareerDialogFragment;
import com.tacademy.depol.profile.dynamicview.CertificationDialogFragment;
import com.tacademy.depol.profile.dynamicview.DynamicAcademyView;
import com.tacademy.depol.profile.dynamicview.DynamicAwordView;
import com.tacademy.depol.profile.dynamicview.DynamicBasicView;
import com.tacademy.depol.profile.dynamicview.DynamicCareerView;
import com.tacademy.depol.profile.dynamicview.DynamicCertificationView;
import com.tacademy.depol.profile.dynamicview.DynamicDialogFragment.IDynamicDialogListener;
import com.tacademy.depol.profile.dynamicview.DynamicInfographicsView;
import com.tacademy.depol.profile.dynamicview.DynamicView;
import com.tacademy.depol.profile.dynamicview.InfographicsDialogFragment;
import com.tacademy.depol.profile.dynamicview.SimpleDynamicViewListener;


public class ProfileInfoFragment<T> extends SherlockFragment {
	
	private DynamicAcademyView dynamicAcademyView;
	private DynamicBasicView dynamicBasicView;
	private DynamicCareerView dynamicCareerView;
	private DynamicCertificationView dynamicCertificationView;
	private DynamicAwordView dynamicAwordView;
	private DynamicInfographicsView dynamicInfographicsView;
	private LoadDialogFragment loadDialogFragment;
	
	private UserItem mData;
	private ArrayList<DateInfo> tempEducationList;
	private ArrayList<DateInfo> tempCareerList;
	private ArrayList<DateInfo> tempCertificationList;
	private ArrayList<DateInfo> tempAwordList;
	private ArrayList<AbilityInfo> tempInfographicsList;
	private boolean isCacheSave = false;
		
	private SimpleNetworkManagerListener<UserItem> mListener = new SimpleNetworkManagerListener<UserItem>() {
		
		public void onLoadDataListener(UserItem item) {
			mData = item;
			
			tempEducationList = SerializationUtils.clone(item.educationInfo);
			tempCareerList = SerializationUtils.clone(item.workInfo);
			tempCertificationList = SerializationUtils.clone(item.certificationInfo);
			tempAwordList = SerializationUtils.clone(item.awardInfo);
			tempInfographicsList = SerializationUtils.clone(item.abilityInfo);
			
			dynamicAcademyView.setData(item.educationInfo, getString(R.string.academic_type));
			dynamicBasicView.setData(item.basicInfo, getString(R.string.basic_type));
			dynamicCareerView.setData(item.workInfo, getString(R.string.career_type));
			dynamicCertificationView.setData(item.certificationInfo, getString(R.string.certification_type));
			dynamicAwordView.setData(item.awardInfo, getString(R.string.aword_type));
			dynamicInfographicsView.setData(item.abilityInfo, getString(R.string.infographics_type));
			
			dynamicAcademyView.populate(DynamicView.INIT_MODE);
			dynamicBasicView.populate(DynamicView.INIT_MODE);
			dynamicCareerView.populate(DynamicView.INIT_MODE);
			dynamicCertificationView.populate(DynamicView.INIT_MODE);
			dynamicAwordView.populate(DynamicView.INIT_MODE);
			dynamicInfographicsView.populate(DynamicView.INIT_MODE);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadDialogFragment = new LoadDialogFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.profile_my_info_layout, container, false);

		dynamicAcademyView = (DynamicAcademyView)v.findViewById(R.id.dynamicAcademyView);
		dynamicBasicView = (DynamicBasicView)v.findViewById(R.id.dynamicBasicView);
		dynamicCareerView = (DynamicCareerView)v.findViewById(R.id.dynamicCareerView);
		dynamicAwordView = (DynamicAwordView)v.findViewById(R.id.dynamicAwordView);
		dynamicCertificationView = (DynamicCertificationView)v.findViewById(R.id.dynamicCertificationView);
		dynamicInfographicsView = (DynamicInfographicsView)v.findViewById(R.id.dynamicInfographicsView);
		
		Bundle bundle = getArguments();
		boolean isMine = bundle.getBoolean(ProfileActivity.PROFILE_TYPE_KEY);
		int userId = bundle.getInt(ProfileActivity.PROFILE_USER_ID_KEY);
		
		if (isMine) {
			UserBasicInfo info = PropertyManager.getInstance().getMyData();
			UserItem item = NetworkManager.getInstance().getCacheUserItem(getActivity(), info.userId, mListener);	
			if (item != null) {
				mData = item;
				tempEducationList = SerializationUtils.clone(item.educationInfo);
				tempCareerList = SerializationUtils.clone(item.workInfo);
				tempCertificationList = SerializationUtils.clone(item.certificationInfo);
				tempAwordList = SerializationUtils.clone(item.awardInfo);
				tempInfographicsList = SerializationUtils.clone(item.abilityInfo);

				dynamicAcademyView.setData(item.educationInfo, getString(R.string.academic_type));
				dynamicBasicView.setData(item.basicInfo, getString(R.string.basic_type));
				dynamicCareerView.setData(item.workInfo, getString(R.string.career_type));
				dynamicCertificationView.setData(item.certificationInfo, getString(R.string.certification_type));
				dynamicAwordView.setData(item.awardInfo, getString(R.string.aword_type));
				dynamicInfographicsView.setData(item.abilityInfo, getString(R.string.infographics_type));
				
				dynamicAcademyView.populate(DynamicView.INIT_MODE);
				dynamicBasicView.populate(DynamicView.INIT_MODE);
				dynamicCareerView.populate(DynamicView.INIT_MODE);
				dynamicCertificationView.populate(DynamicView.INIT_MODE);
				dynamicAwordView.populate(DynamicView.INIT_MODE);
				dynamicInfographicsView.populate(DynamicView.INIT_MODE);
			}
		} else {
			UserItem item = NetworkManager.getInstance().getCacheOtherProfile(getActivity(), userId);
			if (item != null) {
				mData = item;
				tempEducationList = SerializationUtils.clone(item.educationInfo);
				tempCareerList = SerializationUtils.clone(item.workInfo);
				tempCertificationList = SerializationUtils.clone(item.certificationInfo);
				tempAwordList = SerializationUtils.clone(item.awardInfo);
				tempInfographicsList = SerializationUtils.clone(item.abilityInfo);

				dynamicAcademyView.setData(item.educationInfo, getString(R.string.academic_type));
				dynamicBasicView.setData(item.basicInfo, getString(R.string.basic_type));
				dynamicCareerView.setData(item.workInfo, getString(R.string.career_type));
				dynamicCertificationView.setData(item.certificationInfo, getString(R.string.certification_type));
				dynamicAwordView.setData(item.awardInfo, getString(R.string.aword_type));
				dynamicInfographicsView.setData(item.abilityInfo, getString(R.string.infographics_type));
				
				dynamicAcademyView.populate(DynamicView.INIT_MODE);
				dynamicBasicView.populate(DynamicView.INIT_MODE);
				dynamicCareerView.populate(DynamicView.INIT_MODE);
				dynamicCertificationView.populate(DynamicView.INIT_MODE);
				dynamicAwordView.populate(DynamicView.INIT_MODE);
				dynamicInfographicsView.populate(DynamicView.INIT_MODE);
			}
			dynamicAcademyView.hideEditButton();
			dynamicBasicView.hideEditButton();
			dynamicCareerView.hideEditButton();
			dynamicCertificationView.hideEditButton();
			dynamicAwordView.hideEditButton();
			dynamicInfographicsView.hideEditButton();
		}
				
		
		//기본정보
		dynamicBasicView.setOnDynamicViewListener(new SimpleDynamicViewListener<BasicInfo>() {
			
			@Override
			public void onSaveClickListener(View v, final BasicInfo data) {
				ServiceAPI.getInstance().RequestBasicInfoModify(getActivity(),data, new SimpleServiceListener(){
					
					@Override
					public void onBasicInfoModifySuccess() {
						PropertyManager.getInstance().setBasicMyData(data);
						((ProfileActivity)getActivity()).basicRefresh(data);
						dynamicBasicView.populate(DynamicView.INIT_MODE);
					}
					
					@Override
					public void onBasicInfoModifyFail(int statusCode) {
						
					}
					
				});
			}
			
			@Override
			public void onEditClickListener(View v) {
				dynamicBasicView.populate(DynamicView.EDIT_MODE);
			}
			
			@Override
			public void onCancelClickListener(View v) {
				dynamicBasicView.populate(DynamicView.INIT_MODE);
			}
		});
		
		//학력사항
		dynamicAcademyView.setOnDynamicViewListener(new SimpleDynamicViewListener<DateInfo>() {

			@Override
			public void onCancelClickListener(View v) {
				dynamicAcademyView.setData(tempEducationList, getString(R.string.academic_type));
				dynamicAcademyView.populate(DynamicView.INIT_MODE);
			}
			
			@Override
			public void onEditClickListener(View v) {
				mData.educationInfo = SerializationUtils.clone(tempEducationList);
				dynamicAcademyView.populate(DynamicView.EDIT_MODE);
			}

			@Override
			public void onSaveClickListener(View v, ArrayList<DateInfo> data) {
				isCacheSave = true;
				loadDialogFragment.show(getChildFragmentManager(), null);
				Gson gson = new Gson();
				JsonData<DateInfo> j = new JsonData<DateInfo>();
				j.count = data.size();
				j.data = data;
				String json = gson.toJson(j);
				ServiceAPI.getInstance().RequestEducationInfoModify(getActivity(), json, new SimpleServiceListener<UserItem>(){
					
					@Override
					public void onEducationInfoModifySuccess(UserItem data) {
						tempEducationList = SerializationUtils.clone(data.educationInfo);
						dynamicAcademyView.setData(tempEducationList, getString(R.string.academic_type));
						dynamicAcademyView.populate(DynamicView.INIT_MODE);
						loadDialogFragment.dismiss();
					}
					
					@Override
					public void onEducationInfoModifyFail(int statusCode) {
						Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
						loadDialogFragment.dismiss();
					}
					
				});
			}

			@Override
			public void onModifyClickListener(View v, DateInfo info) {
				AcademicDialogFragment academicDialogFragment = new AcademicDialogFragment(info);
				academicDialogFragment.show(getChildFragmentManager(), AcademicDialogFragment.REQUEST_MODIFY_MODE);
				academicDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {

						updateData(dynamicAcademyView.getData(), data);
						dynamicAcademyView.populate(DynamicView.EDIT_MODE);
					}
				});
			}

			@Override
			public void onRemoveClickListener(View v, DateInfo info) {
				dynamicAcademyView.getData().remove(info);
				dynamicAcademyView.populate(DynamicView.EDIT_MODE);	
			}

			@Override
			public void onAddClickListener(View v) {
				AcademicDialogFragment academicDialogFragment = new AcademicDialogFragment();
				academicDialogFragment.show(getChildFragmentManager(), AcademicDialogFragment.REQUEST_ADD_MODE);
				academicDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						dynamicAcademyView.getData().add(data);
						dynamicAcademyView.populate(DynamicView.EDIT_MODE);	
					}
				});
			}
		});
		

		//경력사항
		dynamicCareerView.setOnDynamicViewListener(new SimpleDynamicViewListener<DateInfo>() {

			@Override
			public void onCancelClickListener(View v) {
				dynamicCareerView.setData(tempCareerList, getString(R.string.career_type));
				dynamicCareerView.populate(DynamicView.INIT_MODE);
			}
			
			@Override
			public void onEditClickListener(View v) {
				mData.workInfo = SerializationUtils.clone(tempCareerList);
				dynamicCareerView.populate(DynamicView.EDIT_MODE);
			}

			@Override
			public void onSaveClickListener(View v, ArrayList<DateInfo> data) {
				isCacheSave = true;
				loadDialogFragment.show(getChildFragmentManager(), null);
				Gson gson = new Gson();
				JsonData<DateInfo> j = new JsonData<DateInfo>();
				j.count = data.size();
				j.data = data;
				String json = gson.toJson(j);
				ServiceAPI.getInstance().RequestWorkInfoModify(getActivity(), json, new SimpleServiceListener<UserItem>(){
					
					@Override
					public void onWorkInfoModifySuccess(UserItem data) {
						tempCareerList = SerializationUtils.clone(data.workInfo);
						dynamicCareerView.setData(tempCareerList, getString(R.string.career_type));
						dynamicCareerView.populate(DynamicView.INIT_MODE);
						loadDialogFragment.dismiss();
					}
					
					
					@Override
					public void onWorkInfoModifyFail(int statusCode) {
						Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
						loadDialogFragment.dismiss();
					}
					
					
				});
			}

			@Override
			public void onModifyClickListener(View v, DateInfo info) {
				CareerDialogFragment academicDialogFragment = new CareerDialogFragment(info);
				academicDialogFragment.show(getChildFragmentManager(), CareerDialogFragment.REQUEST_MODIFY_MODE);
				academicDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						updateData(dynamicCareerView.getData(), data);
						dynamicCareerView.populate(DynamicView.EDIT_MODE);
					}
				});
			}

			@Override
			public void onRemoveClickListener(View v, DateInfo info) {
				dynamicCareerView.getData().remove(info);
				dynamicCareerView.populate(DynamicView.EDIT_MODE);	
			}

			@Override
			public void onAddClickListener(View v) {
				CareerDialogFragment academicDialogFragment = new CareerDialogFragment();
				academicDialogFragment.show(getChildFragmentManager(), CareerDialogFragment.REQUEST_ADD_MODE);
				academicDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						dynamicCareerView.getData().add(data);
						dynamicCareerView.populate(DynamicView.EDIT_MODE);	
					}
				});
			}
		});
		


		//자격사항
		dynamicCertificationView.setOnDynamicViewListener(new SimpleDynamicViewListener<DateInfo>() {

			@Override
			public void onCancelClickListener(View v) {
				dynamicCertificationView.setData(tempCertificationList, getString(R.string.certification_type));
				dynamicCertificationView.populate(DynamicView.INIT_MODE);
			}
			
			@Override
			public void onEditClickListener(View v) {
				mData.certificationInfo = SerializationUtils.clone(tempCertificationList);
				dynamicCertificationView.populate(DynamicView.EDIT_MODE);
			}

			@Override
			public void onSaveClickListener(View v, ArrayList<DateInfo> data) {
				isCacheSave = true;
				loadDialogFragment.show(getChildFragmentManager(), null);
				Gson gson = new Gson();
				JsonData<CertificationInfo> info = new JsonData<CertificationInfo>();
				info.data = new ArrayList<CertificationInfo>();
				for(int i=0;i<data.size();i++) {
					info.data.add(new CertificationInfo(data.get(i).pk, data.get(i).text));
				}
				info.count = data.size();
				
				String json = gson.toJson(info);
				ServiceAPI.getInstance().RequestCertificationInfoModify(getActivity(), json, new SimpleServiceListener<UserItem>(){

					@Override
					public void onCertificationInfoModifySuccess(UserItem data) {
						tempCertificationList = SerializationUtils.clone(data.certificationInfo);
						dynamicCertificationView.setData(tempCertificationList, getString(R.string.certification_type));
						dynamicCertificationView.populate(DynamicView.INIT_MODE);
						loadDialogFragment.dismiss();
					}
					
					@Override
					public void onCertificationInfoModifyFail(int statusCode) {
						Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
						loadDialogFragment.dismiss();
					}
					
				});
				
				
				
			}

			@Override
			public void onModifyClickListener(View v, DateInfo info) {
				CertificationDialogFragment certificationDialogFragment = new CertificationDialogFragment(info);
				certificationDialogFragment.show(getChildFragmentManager(), CertificationDialogFragment.REQUEST_MODIFY_MODE);
				certificationDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						updateData(dynamicCertificationView.getData(), data);
						dynamicCertificationView.populate(DynamicView.EDIT_MODE);
					}
				});
			}

			@Override
			public void onRemoveClickListener(View v, DateInfo info) {
				dynamicCertificationView.getData().remove(info);
				dynamicCertificationView.populate(DynamicView.EDIT_MODE);	
			}

			@Override
			public void onAddClickListener(View v) {
				CertificationDialogFragment certificationDialogFragment = new CertificationDialogFragment();
				certificationDialogFragment.show(getChildFragmentManager(), CertificationDialogFragment.REQUEST_ADD_MODE);
				certificationDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {

					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						dynamicCertificationView.getData().add(data);
						dynamicCertificationView.populate(DynamicView.EDIT_MODE);
					}
					
				});
			}
		});
				
		
		//수상내역
		dynamicAwordView.setOnDynamicViewListener(new SimpleDynamicViewListener<DateInfo>(){
			
			@Override
			public void onCancelClickListener(View v) {
				dynamicAwordView.setData(tempAwordList, getString(R.string.aword_type));
				dynamicAwordView.populate(DynamicView.INIT_MODE);
			}
			
			@Override
			public void onEditClickListener(View v) {
				mData.awardInfo = SerializationUtils.clone(tempAwordList);
				dynamicAwordView.populate(DynamicView.EDIT_MODE);
			}

			@Override
			public void onSaveClickListener(View v, ArrayList<DateInfo> data) {
				isCacheSave = true;
				loadDialogFragment.show(getChildFragmentManager(), null);
				Gson gson = new Gson();
				JsonData<AwordInfo> info = new JsonData<AwordInfo>();
				info.data = new ArrayList<AwordInfo>();
				for(int i=0;i<data.size();i++) {
					info.data.add(new AwordInfo(data.get(i).pk, data.get(i).year, data.get(i).text, data.get(i).subText));
				}
				info.count = data.size();
				String json = gson.toJson(info);
				ServiceAPI.getInstance().RequestAwordInfoModify(getActivity(), json, new SimpleServiceListener<UserItem>(){
					

					@Override
					public void onAwardInfoModifyFail(int statusCode) {

						Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
						loadDialogFragment.dismiss();
					}
					@Override
					public void onAwardInfoModifySuccess(UserItem data) {
						tempAwordList = SerializationUtils.clone(data.awardInfo);
						dynamicAwordView.setData(tempAwordList, getString(R.string.aword_type));
						dynamicAwordView.populate(DynamicView.INIT_MODE);
						loadDialogFragment.dismiss();
					}
				});
				
				
				
			}

			@Override
			public void onModifyClickListener(View v, DateInfo info) {
				AwordDialogFragment awordDialogFragment = new AwordDialogFragment(info);
				awordDialogFragment.show(getChildFragmentManager(), CareerDialogFragment.REQUEST_MODIFY_MODE);
				awordDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						updateData(dynamicAwordView.getData(), data);
						dynamicAwordView.populate(DynamicView.EDIT_MODE);
					}
				});
			}

			@Override
			public void onRemoveClickListener(View v, DateInfo info) {
				dynamicAwordView.getData().remove(info);
				dynamicAwordView.populate(DynamicView.EDIT_MODE);	
			}

			@Override
			public void onAddClickListener(View v) {
				AwordDialogFragment awordDialogFragment = new AwordDialogFragment();
				awordDialogFragment.show(getChildFragmentManager(), CertificationDialogFragment.REQUEST_ADD_MODE);
				awordDialogFragment.setOnDynamicDialogListener(new IDynamicDialogListener() {

					@Override
					public void onDynamicDialogVerify(DateInfo data) {
						dynamicAwordView.getData().add(data);
						dynamicAwordView.populate(DynamicView.EDIT_MODE);
					}
					
				});
			}
			
		});
		
		/*
		 * 인포그래픽
		 */
		dynamicInfographicsView.setOnDynamicViewListener(new SimpleDynamicViewListener<AbilityInfo>(){
			
			@Override
			public void onCancelClickListener(View v) {
				dynamicInfographicsView.setData(tempInfographicsList, getString(R.string.infographics_type));
				dynamicInfographicsView.populate(DynamicView.INIT_MODE);
			}
			
			@Override
			public void onEditClickListener(View v) {
				mData.abilityInfo = SerializationUtils.clone(tempInfographicsList);
				dynamicInfographicsView.populate(DynamicView.EDIT_MODE);
			}

			@Override
			public void onSaveClickListener(View v, ArrayList<AbilityInfo> data) {
				isCacheSave = true;
				loadDialogFragment.show(getChildFragmentManager(), null);
				Gson gson = new Gson();
				JsonData<AbilityInfo> jsondata = new JsonData<AbilityInfo>();
				jsondata.count = data.size();
				jsondata.data = data;
				String json = gson.toJson(jsondata);
				ServiceAPI.getInstance().RequestAbilityInfoModify(getActivity(), json, new SimpleServiceListener<UserItem>(){
					
					@Override
					public void onAbilityInfoModifySuccess(UserItem data) {
						tempInfographicsList = SerializationUtils.clone(data.abilityInfo);
						dynamicInfographicsView.setData(tempInfographicsList, getString(R.string.infographics_type));
						dynamicInfographicsView.populate(DynamicView.INIT_MODE);
						loadDialogFragment.dismiss();
					}
					
					@Override
					public void onAbilityInfoModifyFail(int statusCode) {
						Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
						loadDialogFragment.dismiss();
					}
					
				});

			}
			
			@Override
			public void onRemoveClickListener(View v, AbilityInfo data) {
				dynamicInfographicsView.getData().remove(data);
				dynamicInfographicsView.populate(DynamicView.EDIT_MODE);
			}
			
			@Override
			public void onAddClickListener(View v) {
				InfographicsDialogFragment infographicsDialog = new InfographicsDialogFragment();
				infographicsDialog.show(getChildFragmentManager(), CertificationDialogFragment.REQUEST_ADD_MODE);
				infographicsDialog.setOnDynamicDialogListener(new InfographicsDialogFragment.IDynamicDialogListener() {
					
					@Override
					public void onDynamicDialogVerify(ArrayList<AbilityInfo> data) {
						dynamicInfographicsView.getData().addAll(data);
						dynamicInfographicsView.populate(DynamicView.EDIT_MODE);
					}
				});
			}
			
		});
		
		return v;		
	}
	
	
	private void updateData(ArrayList<DateInfo> beforeData, DateInfo afterData) {
		for (int i=0;i<beforeData.size();i++) {
			DateInfo info = beforeData.get(i);
			if (info.pk == afterData.pk) {
				beforeData.remove(i);
				beforeData.add(i, afterData);
			}
		}
	}
	
	// 원본 temp / 변경 data 비교 PK 추출     (ex 4개 / 2개)
	private ArrayList<Integer> checkRemoveItem(ArrayList<DateInfo> temp, ArrayList<DateInfo> data) {
		
		ArrayList<Integer> pkList = new ArrayList<Integer>();
		for(int i=0;i<temp.size();i++) {
			boolean isRemoved = true;
			for (int j=0;j<data.size();j++) {
				if (i == j) {
					isRemoved = false;
					break;
				}
			}
			if (isRemoved) {
				int removePk = temp.get(i).pk;
				pkList.add(removePk);
			}
		}
		
		return pkList;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isCacheSave) {
			int id = PropertyManager.getInstance().getMyData().userId;
			NetworkManager.getInstance().getUserProfile(getActivity(), id, true, null);
		}
	}
}

package com.tacademy.depol.model;

import com.tacademy.depol.data.UserBasicInfo;

interface IServiceAPIListener<T> {
	public void onServiceSuccessListener(T data);
	public void onServiceFailListener(int statusCode);
	public void onLoginSuccess(T data);
	public void onLoginFail(int statusCode);
	public void onLoginPasswordFail(int statusCode);
	public void onLoginIdFail(int statusCode);
	public void onFileUploadSuccess();
	public void onFileUploadFail(int statusCode);
	public void onSiginUpFail(int statusCode);
	public void onSiginUpSuccess();
	public void onBasicInfoModifySuccess();
	public void onBasicInfoModifyFail(int statusCode);
	public void onEducationInfoModifySuccess(T data);
	public void onEducationInfoModifyFail(int statusCode);
	public void onWorkInfoModifySuccess(T data);
	public void onWorkInfoModifyFail(int statusCode);
	public void onCertificationInfoModifySuccess(T data);
	public void onCertificationInfoModifyFail(int statusCode);
	public void onAwardInfoModifySuccess(T data);
	public void onAwardInfoModifyFail(int statusCode);
	public void onAbilityInfoModifySuccess(T data);
	public void onAbilityInfoModifyFail(int statusCode);
	public void onProgress(int bytesWritten, int totalSize);
	public void onPortfolioRemoveSuccess();
	public void onPortfolioRemoveFail(int statusCode);
	public void onPortfolioUpdateSuccess();
	public void onPortfolioUpdateFail(int statusCode);
	public void onProfileUpdateSuccess(T data);
	public void onProfileUpdateFail(int statusCode);
	public void onPortfolioCommentRequestSuccess(T data);
	public void onPortfolioCommentRequestFail(int statusCode);
	public void onPortfolioCommentWriteRequestSuccess(T data);
	public void onPortfolioCommentWriteRequestFail(int statusCode);
	public void onPortfolioCommentRemoveRequestSuccess(T data);
	public void onPortfolioCommentRemoveRequestFail(int statusCode);
	public void onPortfolioLikeRequestSuccess();
	public void onPortfolioLikeRequestFail(int statusCode);
	public void onPortfolioUnLikeRequestSuccess();
	public void onPortfolioUnLikeRequestFail(int statusCode);
	public void onPortfolioLikeUserRequestSuccess(T data);
	public void onPortfolioLikeUserRequestFail(int statusCode);
	public void onFollowRequestSuccess();
	public void onFollowRequestFail(int statusCode);
	public void onUnFollowRequestSuccess();
	public void onUnFollowRequestFail(int statusCode);
	public void onFollowListRequestSuccess(T data);
	public void onFollowListRequestFail(int statusCode);
	public void onFollowIngListRequestSuccess(T data);
	public void onFollowIngListRequestFail(int statusCode);
	public void onMessageListRequestSuccess(T data);
	public void onMessageListRequestFail(int statusCode);
	public void onMessageReadRequestSuccess();
	public void onMessageReadRequestFail(int statusCode);
	public void onMessageSendRequestSuccess();
	public void onMessageSendRequestFail(int statusCode);
	public void onLikeListRequestSuccess(T data);
	public void onLikeListRequestFail(int statusCode);
	public void onLikeReadRequestSuccess(int likeId);
	public void onLikeReadRequestFail(int statusCode);
	public void onMessageRemoveRequestSuccess(int messageId);
	public void onMessageRemoveRequestFail(int statusCode);
	public void onSearchRequestSuccess(T data);
	public void onSearchRequestFail(int statusCode);
	public void onMenuNotiRequestSuccess(T data);
	public void onMenuNotiRequestFail(int statusCode);
	public void onEmailModifyRequestSuccess(T data);
	public void onEmailModifyRequestFail(int statusCode);
	public void onPasswordModifyRequestFail(int statusCode);
	public void onPasswordModifyRequestOldPassFail();
	public void onPasswordModifyRequestSuccess();

	public void onNoticeRequestFail(int statusCode);
	public void onNoticeRequestSuccess(T data);
	

	public void onVersionRequestFail(int statusCode);
	public void onVersionRequestSuccess(String version);
}

public class SimpleServiceListener<T> implements IServiceAPIListener<T> {

	@Override
	public void onServiceSuccessListener(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceFailListener(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileUploadSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileUploadFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSiginUpFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSiginUpSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBasicInfoModifySuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBasicInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onEducationInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWorkInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWorkInfoModifySuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEducationInfoModifySuccess(T data) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCertificationInfoModifySuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCertificationInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAwardInfoModifySuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAwardInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAbilityInfoModifySuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAbilityInfoModifyFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioRemoveSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioRemoveFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioUpdateSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioUpdateFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProfileUpdateFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProfileUpdateSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentWriteRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentWriteRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentRemoveRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioCommentRemoveRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioLikeRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioLikeRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioUnLikeRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioUnLikeRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioLikeUserRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioLikeUserRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnFollowRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowListRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowListRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowIngListRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowIngListRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFollowRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnFollowRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageListRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageListRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReadRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReadRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageSendRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageSendRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLikeListRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLikeListRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLikeReadRequestSuccess(int likeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLikeReadRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageRemoveRequestSuccess(int messageId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageRemoveRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSearchRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSearchRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMenuNotiRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMenuNotiRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEmailModifyRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEmailModifyRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPasswordModifyRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPasswordModifyRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPasswordModifyRequestOldPassFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoticeRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNoticeRequestSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVersionRequestFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVersionRequestSuccess(String version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginPasswordFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginIdFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}


	

}

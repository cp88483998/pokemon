package pocket.chat.entity;

public class OfflineMsg {
	private String senderUid;
	private String recipientUid;
	private String context;
	private String additionalMsg;
	public String getSenderUid() {
		return senderUid;
	}
	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}
	public String getRecipientUid() {
		return recipientUid;
	}
	public void setRecipientUid(String recipientUid) {
		this.recipientUid = recipientUid;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getAdditionalMsg() {
		return additionalMsg;
	}
	public void setAdditionalMsg(String additionalMsg) {
		this.additionalMsg = additionalMsg;
	}
	@Override
	public String toString() {
		return "OfflineMsg [senderUid=" + senderUid + ", recipientUid=" + recipientUid + ", context=" + context
				+ ", additionalMsg=" + additionalMsg + "]";
	}
	
}

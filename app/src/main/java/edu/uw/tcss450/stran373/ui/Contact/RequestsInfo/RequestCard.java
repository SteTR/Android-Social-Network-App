package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import java.io.Serializable;

/**
 * Request Card contains a builder class to create cards
 * that will be shown in the recyclerview
 * @date 05/19/2021
 * @author Andrew Bennett
 */
public class RequestCard implements Serializable {

    /**
     * First name of the person requesting a connection
     */
    private final String mFirstName;
    private final String mLastName;
    private final String mMemberID;

    /**
     * This field is used to check if the invite was accepted or declined.
     */
    private boolean isAccepted;

    /**
     * This field is used to check if request was accepted or declined
     */
    private boolean isDeclined;

    /**
     * Set whether the contact is selected or not
     */
    public void setAccepted(boolean theChecked) { this.isAccepted = theChecked;}
    /**
     * Set whether the contact is selected or not
     */
    public void setDeclined(boolean theChecked) { this.isDeclined= theChecked;}

    /**
     * Necessary builder function to build the Request card
     * @author Andrew Bennett
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private String mMemberID;
        private boolean isAccepted;
        private boolean isDeclined;

        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mMemberID = "-1";
        }

        public Builder(final String theContactID, final String theFirstName, final String theLastName)
        {
            this.mMemberID = theContactID;
            this.mFirstName = theFirstName;
            this.mLastName = theLastName;
            this.isAccepted = false;
            this.isDeclined = false;
        }


        /**
         * Sets the first name
         * @param theFirstName first name of the Request card
         */
        public void setFirstName(final String theFirstName) {
            mFirstName = theFirstName;
        }

        /**
         * Sets the last name
         * @param theLastName returns the last name
         */
        public void setLastName(final String theLastName) {
            mLastName = theLastName;
        }

        /**
         * Set the contact id of the contact card
         * @param theMemberID contactid of this contact
         */
        public void setMemberId(final String theMemberID) {
            this.mMemberID = theMemberID;
        }


        /**
         * Builds the request card
         * @return RequestCard return the completed request card.
         */
        public RequestCard build()
        {
            return new RequestCard(this);
        }
    }

    private RequestCard(final RequestCard.Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mMemberID = builder.mMemberID;
    }

    /**
     * Returns the id corresponding to the contact
     * @return Integer chat id
     */
    public String getMemberID()
    {
        return mMemberID;
    }

    /**
     * Returns the first name of contact
     * @return mFirstName
     */
    public String getFirstName(){ return mFirstName;}

    /**
     * Returns the last name of contact
     * @return mLastName
     */
    public String getLastName(){return mLastName;}

    public boolean getAccepted(){return isAccepted;}

    public boolean getDeclined(){return isDeclined;}
}

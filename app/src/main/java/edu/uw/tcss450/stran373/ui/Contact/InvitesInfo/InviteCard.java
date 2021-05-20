package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import java.io.Serializable;

/**
 * Invite class contains name, id, and email of an invitee
 * @author Andrew Bennett
 */
public class InviteCard implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final String mMemberID;
    private boolean isSelected;



    /**
     * Necessary builder function to build the contact card
     * @author Andrew Bennett
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private String mMemberID;
        private boolean isSelected;

        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mMemberID = "-1";
            isSelected = false;
        }

        public Builder(final String theContactID, final String theFirstName, final String theLastName)
        {
            this.mMemberID = theContactID;
            this.mFirstName = theFirstName;
            this.mLastName = theLastName;
            this.isSelected = false;
        }

        /**
         * Sets the first name
         * @author Andrew Bennett
         * @param theFirstName first name of the contact card
         */
        public void setFirstName(final String theFirstName) {
            mFirstName = theFirstName;
        }

        /**
         * Sets the last name
         * @author Andrew Bennett
         * @param theLastName returns the last name
         */
        public void setLastName(final String theLastName) {
            mLastName = theLastName;
        }

        /**
         * Set the contact id of the contact card
         *
         * @param theContactID contactid of this contact
         */
        public void setMemberId(final String theContactID) {
            this.mMemberID = theContactID;
        }


        /**
         * Builds the contact card
         * @return Contact
         */
        public InviteCard build()
        {
            return new InviteCard(this);
        }
    }

    /**
     * Builds the Invite
     * @param builder Builder for invite cards
     */
    private InviteCard(final Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mMemberID = builder.mMemberID;
    }

    /**
     * Returns the id corresponding to the contact of the contact you're inviting
     * @return Integer chat id
     */
    public String getMemberID()
    {
        return mMemberID;
    }

    /**
     * Returns the first name of contact of you're inviting
     * @return mFirstName
     */
    public String getFirstName(){ return mFirstName;}

    /**
     * Returns the last name of contact you're inviting
     * @return mLastName
     */
    public String getLastName(){return mLastName;}


    /**
     * Returns whether or not the checkbox is selected.
     * @return true/false depending on if this specific card is checked
     */
    public boolean isSelected(){return isSelected;}

    public void setSelected(boolean theChecked) { this.isSelected = theChecked;}

}
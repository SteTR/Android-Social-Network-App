package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import java.io.Serializable;

/**
 * Invite class contains name, id, and email of an invitee
 * @author Andrew Bennett
 */
public class InviteCard implements Serializable {

    /**
     * Represents the first name.
     */
    private final String mFirstName;

    /**
     * Represents the last name.
     */
    private final String mLastName;

    /**
     * Represents the member ID.
     */
    private final String mMemberID;

    /**
     * Checks if the checkbox is selected.
     */
    private boolean isSelected;

    /**
     * Necessary builder function to build the contact card
     * @author Andrew Bennett
     */
    public static class Builder {

        /**
         * Represents the first name.
         */
        private String mFirstName;

        /**
         * Represents the last name.
         */
        private String mLastName;

        /**
         * Represents the member ID.
         */
        private String mMemberID;

        /**
         * Represents the selected  (checkbox).
         */
        private boolean isSelected;

        /**
         * Default constructor for the Builder.
         */
        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mMemberID = "-1";
            isSelected = false;
        }

        /**
         * Parameterized Builder constructor for an Invite Card.
         *
         * @param theContactID is the contact ID.
         * @param theFirstName is the first name.
         * @param theLastName is the last name.
         */
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
     * @param theBuilder Builder for invite cards
     */
    private InviteCard(final Builder theBuilder)
    {
        this.mFirstName = theBuilder.mFirstName;
        this.mLastName = theBuilder.mLastName;
        this.mMemberID = theBuilder.mMemberID;
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

    /**
     * Used to change its selected status (checkbox).
     *
     * @param theChecked the selected status
     */
    public void setSelected(boolean theChecked) { this.isSelected = theChecked;}

}
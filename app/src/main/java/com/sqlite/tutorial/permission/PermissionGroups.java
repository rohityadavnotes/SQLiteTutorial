package com.sqlite.tutorial.permission;

public class PermissionGroups {

    public static final int PERMISSIONS_FROM_SETTING_REQUEST_CODE       = 2000;
    public static final int MULTIPLE_PERMISSIONS_GROUP_REQUEST_CODE     = 2001;

    public static final int CALENDAR_GROUP_REQUEST_CODE                 = 2002;
    public static final String READ_CALENDAR                            = android.Manifest.permission.READ_CALENDAR;
    public static final String WRITE_CALENDAR                           = android.Manifest.permission.WRITE_CALENDAR;
    public static final String[] CALENDAR_GROUP                         = new String[]
            {
                    READ_CALENDAR,
                    WRITE_CALENDAR
            };

    public static final int CAMERA_GROUP_REQUEST_CODE                   = 2003;
    public static final String CAMERA                                   = android.Manifest.permission.CAMERA;
    public static final String[] CAMERA_GROUP                           = new String[]
            {
                    CAMERA
            };

    public static final int CONTACTS_GROUP_REQUEST_CODE                 = 2004;
    public static final String READ_CONTACTS                            = android.Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS                           = android.Manifest.permission.WRITE_CONTACTS;
    public static final String GET_ACCOUNTS                             = android.Manifest.permission.GET_ACCOUNTS;
    public static final String[] CONTACTS_GROUP                         = new String[]
            {
                    READ_CONTACTS,
                    WRITE_CONTACTS,
                    GET_ACCOUNTS
            };

    public static final int LOCATION_GROUP_REQUEST_CODE                 = 2005;
    public static final String ACCESS_FINE_LOCATION                     = android.Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION                   = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String[] LOCATION_GROUP                         = new String[]
            {
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
            };

    public static final int MICROPHONE_GROUP_REQUEST_CODE               = 2006;
    public static final String RECORD_AUDIO                             = android.Manifest.permission.RECORD_AUDIO;
    public static final String[] MICROPHONE_GROUP                       = new String[]
            {
                    RECORD_AUDIO
            };

    public static final int PHONE_GROUP_REQUEST_CODE                    = 2007;
    public static final String READ_PHONE_STATE                         = android.Manifest.permission.READ_PHONE_STATE;
    public static final String CALL_PHONE                               = android.Manifest.permission.CALL_PHONE;
    public static final String READ_CALL_LOG                            = android.Manifest.permission.READ_CALL_LOG;
    public static final String WRITE_CALL_LOG                           = android.Manifest.permission.WRITE_CALL_LOG;
    public static final String ADD_VOICEMAIL                            = android.Manifest.permission.ADD_VOICEMAIL;
    public static final String USE_SIP                                  = android.Manifest.permission.USE_SIP;
    public static final String PROCESS_OUTGOING_CALLS                   = android.Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String[] PHONE_GROUP                            = new String[]
            {
                    READ_PHONE_STATE,
                    CALL_PHONE,
                    READ_CALL_LOG,
                    WRITE_CALL_LOG,
                    ADD_VOICEMAIL,
                    USE_SIP,
                    PROCESS_OUTGOING_CALLS
            };

    public static final int SENSORS_GROUP_REQUEST_CODE                  = 2008;
    public static final String BODY_SENSORS                             = android.Manifest.permission.BODY_SENSORS;
    public static final String[] SENSORS_GROUP                          = new String[]
            {
                    BODY_SENSORS
            };

    public static final int SMS_GROUP_REQUEST_CODE                      = 2009;
    public static final String SEND_SMS                                 = android.Manifest.permission.SEND_SMS;
    public static final String RECEIVE_SMS                              = android.Manifest.permission.RECEIVE_SMS;
    public static final String READ_SMS                                 = android.Manifest.permission.READ_SMS;
    public static final String RECEIVE_WAP_PUSH                         = android.Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String RECEIVE_MMS                              = android.Manifest.permission.RECEIVE_MMS;
    public static final String[] SMS_GROUP                              = new String[]
            {
                    SEND_SMS,
                    RECEIVE_SMS,
                    READ_SMS,
                    RECEIVE_WAP_PUSH,
                    RECEIVE_MMS
            };

    public static final int STORAGE_GROUP_REQUEST_CODE                  = 2010;
    public static final String READ_EXTERNAL_STORAGE                    = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE                   = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String[] STORAGE_GROUP                          = new String[]
            {
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
            };
}

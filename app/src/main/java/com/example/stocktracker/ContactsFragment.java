package com.example.stocktracker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the ContactsFragment class. It is used to display the contacts
 *               in the phone. It is also used to send a message to the selected contact.
 *               It works with gmail, Whatsapp, or any other messaging app.
 */
public class ContactsFragment extends Fragment {
    private Uri uri = null;
    private Activity containerActivity = null;
    private View inflatedView = null;

    private ListView contactsListView;
    ArrayAdapter<String> contactsAdapter = null;
    private ArrayList<String> contacts = new ArrayList<String>();
    private ArrayList<String> contactsID = new ArrayList<String>();
    private ArrayList<Integer> selectedIndexList = new ArrayList<Integer>();

    /*
     * Sets the container activity when object is first instantiated
     */
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    /*
     * onCreateView method for the class
     */
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsListView = (ListView) inflatedView.findViewById(R.id.contact_list_view);
        TextView counter = (TextView) inflatedView.findViewById(R.id.sendEmailText);
        counter.setText(getResources().getString(R.string.sendEmailText)
                + " ("+selectedIndexList.size()+ ")");
        Button sendMailButton = (Button) inflatedView.findViewById(R.id.sendEmailButton);
        sendMailButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               sendEmail();
           }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String uri_string = bundle.getString("uri");
            uri = Uri.parse(uri_string);
        }
        contactsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?>
                                            parent, View view, int position, long id) {

                if (selectedIndexList.contains(position)) {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    selectedIndexList.remove(selectedIndexList.indexOf(position));
                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.theme_light_blue2));
                    selectedIndexList.add(position);
                }
                counter.setText(getResources().getString(R.string.sendEmailText)
                        + " ("+selectedIndexList.size()+ ")");
            }
        });
        return inflatedView;
    }

    @Override
    /*
     * onCreate method for the class
     */
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getContacts();
    }

    @Override
    /*
     * onResume method for the class
     */
    public void onResume() {
        super.onResume();
        setupContactsAdapter();
    }

    /*
     * sendEmail method fires up an intent and sends an email with the screenshot of the
     * canvas to the specified emailAdress.
     */
    public void sendEmail() {
        // get the email address
        int n = selectedIndexList.size();
        String emails[] = new String[n];
        for (int i = 0 ; i < n ;i++){
            String email = getContactEmail(contactsID.get(selectedIndexList.get(i)));
            if (email == null)
                emails[i] = "";
            else
                emails[i] = email;

        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    /*
     * getContactEmail gets an id as a parameter and returns the associated email
     */
    @SuppressLint("Range")
    public String getContactEmail(String contactID) {
        String email = "";
        Cursor emailCur = containerActivity.getContentResolver().
                query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{contactID}, null);
        while (emailCur.moveToNext()) {
            email = emailCur.getString(emailCur.
                    getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emailCur.close();
        return email;
    }

    /*
     * getContacts gets all the contacts of the device up to a limit of 1000 contacts
     */
    public void getContacts() {
        int limit = 1000;
        Cursor cursor = containerActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,null,
                null, null, null);
        while (cursor.moveToNext() && limit > 0) {
            @SuppressLint("Range") String id = cursor.getString(
                    cursor.getColumnIndex
                            (ContactsContract.Contacts._ID));
            @SuppressLint("Range") String given = cursor.getString(
                    cursor.getColumnIndex
                            (ContactsContract.Contacts.DISPLAY_NAME));
            if( getContactEmail(id) != "") {
                contacts.add(given);
                contactsID.add(id);
                limit--;
            }
        }
        cursor.close();
    }

    /*
     * Method that sets up the ContactsAdapter
     */ 
    private void setupContactsAdapter() {
        contactsListView =
                (ListView) inflatedView.findViewById(R.id.contact_list_view);
        contactsAdapter = new
                ArrayAdapter<String>(containerActivity, R.layout.contact_row,
                R.id.contact_row_text_view, contacts);
        contactsListView.setAdapter(contactsAdapter);
    }

}
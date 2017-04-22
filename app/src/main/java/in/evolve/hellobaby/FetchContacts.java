package in.evolve.hellobaby;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brekkishhh on 22-04-2017.
 */
public class FetchContacts extends AsyncTask<Void,Void,Void> {

    private ContactsRetrieved callBack;
    private Context context;
    private JSONArray informationList;
    public FetchContacts(Context context,ContactsRetrieved callBack) {
        this.context = context;
        this.callBack = callBack;
        this.informationList =  new JSONArray();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {



        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);


        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    pCur.moveToFirst();

                    String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.));
                    try{
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name",name);
                        jsonObject.put("phone",phone);
                        informationList.put(jsonObject);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    pCur.close();
                }
            }
        }
        cursor.close();


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        callBack.workDone(informationList);
    }
}

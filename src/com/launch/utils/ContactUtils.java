package com.launch.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.launch.sqlite.Contact;
import com.launch.sqlite.P;
import com.launch.ui.R;
import com.launch.ui.UserApplication;

public class ContactUtils {

	public static final String[] communaute_groups = {"C_Enfant","C_Voisin", "C_Ami", "C_Aide_Domicile","C_Soignants"};
	
	public static long wirtePtoContact(Contact contact, Context context) {
		ContentValues values = new ContentValues();
		/*
		 * ������RawContacts.CONTENT_URIִ��һ����ֵ���룬Ŀ���ǻ��ϵͳ���ص�rawContactId
		 */
		Uri rawContactUri = context.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// ��data����д���������
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); // ��������
		values.put(StructuredName.GIVEN_NAME, contact.getName());
		
		context.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// ��data����д��绰���
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, contact.getPhone());
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		context.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// ��data����д��Email�����
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.DATA, contact.getEmail());
		values.put(Email.TYPE, Email.TYPE_WORK);
		context.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		return rawContactId;
	}

	public static boolean updateContact(Context context,Contact contact) 
    {
        boolean success = true;

        try
        {

        if(contact.getName().equals("")&&contact.getPhone().equals("")&&contact.getEmail().equals(""))
         {
            success = false;
         }
        else 
        {
            ContentResolver contentResolver  = context.getContentResolver();

            String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"; 

            String[] emailParams = new String[]{String.valueOf(contact.getPhoneContactId()), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}; 
            String[] nameParams = new String[]{String.valueOf(contact.getPhoneContactId()), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}; 
            String[] numberParams = new String[]{String.valueOf(contact.getPhoneContactId()), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE}; 

            ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,emailParams)
                  .withValue(Email.DATA, contact.getEmail())
                  .build());
             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,nameParams)
                  .withValue(StructuredName.DISPLAY_NAME, contact.getName())
                  .build());

             ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                  .withSelection(where,numberParams)
                  .withValue(Phone.NUMBER, contact.getPhone())
                  .build());
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
         }
        }
        catch (Exception e) 
        {
         e.printStackTrace();
         success = false;
        }
        return success;
    }
	
	public static String getContactPhone(ContentResolver cr, Cursor cursor) {

		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		// System.out.print(phoneNum);
		if (phoneNum > 0) {
			// �����ϵ�˵�ID��
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// �����ϵ�˵ĵ绰�����cursor;
			Cursor phones = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			// int phoneCount = phones.getCount();
			// allPhoneNum = new ArrayList<String>(phoneCount);
			if (phones.moveToFirst()) {
				// �������еĵ绰����
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);
					// switch (phone_type) {
					// case 2:
					phoneResult = phoneNumber;
					// break;
					// }
					// allPhoneNum.add(phoneNumber);
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult;
	}

	public static String getContactName(ContentResolver cr, Cursor cursor) {

		return cursor.getString(cursor.getColumnIndexOrThrow(People.NAME));
	}

	public static String getContactEmail(ContentResolver cr, Cursor cursor) {
		// �����ϵ�˵�ID��
		int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		String contactId = cursor.getString(idColumn);
		// �����ϵ�˵ĵ绰�����cursor;
		Cursor emails = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
						+ contactId, null, null);
		// int phoneCount = phones.getCount();
		// allPhoneNum = new ArrayList<String>(phoneCount);
		String emailResult = "";
		if (emails.moveToFirst()) {
			// �������еĵ绰����
			for (; !emails.isAfterLast(); emails.moveToNext()) {
				int index = emails
						.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
				// int typeindex = phones
				// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
				// int phone_type = phones.getInt(typeindex);
				String email = emails.getString(index);
				// switch (phone_type) {
				// case 2:
				emailResult = email;
				// break;
				// }
				// allPhoneNum.add(phoneNumber);
			}
			if (!emails.isClosed()) {
				emails.close();
			}
		}
		return emailResult;
	}

	public static void getPhoneImage(ImageButton image, P p, Context context,
			ContentResolver cr) {
		if (p == null || TextUtils.isEmpty(p.number)) {
			return;
		}
		Uri uriNumber2Contacts = Uri.parse("content://com.android.contacts/"
				+ "data/phones/filter/" + p.number);
		Cursor cursorCantacts = cr.query(uriNumber2Contacts, null, null, null,
				null);
		if (cursorCantacts.getCount() > 0) { // ���α겻Ϊ0��˵����ͷ��,�α�ָ���һ����¼
			cursorCantacts.moveToFirst();
			Long contactID = cursorCantacts.getLong(cursorCantacts
					.getColumnIndex("contact_id"));
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, contactID);
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(cr, uri);
			if (input == null) {
				image.setImageResource(R.drawable.person);
				return;
			}
			image.setImageBitmap(BitmapFactory.decodeStream(input));
		} else {// ô��ͷ������Ĭ��ͷ��
			image.setImageResource(R.drawable.person);
		}
	}

	public static void initContacts(Context context, ContentResolver cr) {
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[] {
				CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
				CallLog.Calls.TYPE, CallLog.Calls.DATE }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		for (String name : cursor.getColumnNames()) {
			System.out.println(name);
		}
		
		Map<String, P> map = new HashMap<String, P>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			if (TextUtils.isEmpty(number)) {
				continue;
			}
			if (TextUtils.isEmpty(name)) {
				name = number;
			}
			if (map.containsKey(number)) {
				P p = map.get(number);
				p.count++;
				map.put(number, p);
			} else {
				P p = new P();
				p.number = number;
				p.name = name;
				p.count = 1;
				map.put(number, p);
			}
		}
		List<P> ps = new ArrayList<P>();
		for (Map.Entry<String, P> entry : map.entrySet()) {
			P p = entry.getValue();
			System.out.println("name : " + p.name + " number : " + p.number
					+ " value : " + p.count);
			ps.add(p);
		}
		Collections.sort(ps, new Comparator<P>() {

			@Override
			public int compare(P P1, P P2) {
				return P2.count - P1.count;
			}

		});

		P p1 = (ps.size() >= 1 ? ps.get(0) : null);

		P p2 = (ps.size() >= 2 ? ps.get(1) : null);

		if (p1 != null) {
			Preferences.savePhonenumber1(context, p1.name, p1.number);
		}
		if (p2 != null) {
			Preferences.savePhonenumber2(context, p2.name, p2.number);
		}
	}
	
	public static boolean readContactDetails(Contact contact, Context context)
	{
		ContentResolver cr = context.getContentResolver();
		Cursor main_cursor = cr.query(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.getPhoneContactId())), null, null, null, null);
		if (main_cursor.moveToFirst()) {
			String name = main_cursor.getString(main_cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			
			Cursor cursor = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contact.getPhoneContactId(), null, null);
			String phone = null;
			while (cursor.moveToNext()) {
				phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				if (!TextUtils.isEmpty(phone)) {
					break;
				}
			}
			cursor.close();
			
			cursor = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contact.getPhoneContactId(), null, null);
			String email = null;
			while (cursor.moveToNext()) {
				email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
				if (!TextUtils.isEmpty(email)) {
					break;
				}
			}
			cursor.close();
			if(!contact.getName().equals(name) || !contact.getPhone().equals(phone) || !contact.getEmail().equals(email))
			{
				contact.setName(name);
				contact.setPhone(phone);
				contact.setEmail(email);
				return true;
			}
		}
		main_cursor.close();
		return false;
	}
	
	public static void createContactGroup(String groupName)
	{
		Cursor groupCursor = null;
        String[] GROUP_PROJECTION = new String[] { ContactsContract.Groups._ID,     ContactsContract.Groups.TITLE };
        groupCursor = UserApplication.getInstance().getActivity().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,    GROUP_PROJECTION, ContactsContract.Groups.TITLE+ "=?", new String[]{groupName}, ContactsContract.Groups.TITLE + " ASC");
        Log.d("*** Here Counts: ","** "+groupCursor.getCount());

        if(groupCursor.getCount() > 0){
            //Toast.makeText(UserApplication.getInstance().getActivity(), "Group is already available", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //Toast.makeText(UserApplication.getInstance().getActivity(), "Not available", Toast.LENGTH_SHORT).show();
        	//Here we create a new Group
            try {
                ContentValues groupValues = null;
                ContentResolver cr = UserApplication.getInstance().getActivity().getContentResolver();
                groupValues = new ContentValues();
                groupValues.put(ContactsContract.Groups.TITLE, groupName);
                cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
                Log.d("########### Group Creation Finished :","###### Success");    
            }
            catch(Exception e){
                Log.d("########### Exception :",""+e.getMessage()); 
            }
            //Toast.makeText(UserApplication.getInstance().getActivity(), "Created Successfully", Toast.LENGTH_SHORT).show();
        }
        groupCursor.close();
        groupCursor = null;
	}
	
	public static ArrayList<P> readContactsGroup(String groupName)
	{
		ArrayList<P> contacts = new ArrayList<P>();
		Cursor groupCursor = null;
        String[] GROUP_PROJECTION = new String[] { ContactsContract.Groups._ID,     ContactsContract.Groups.TITLE };
        groupCursor = UserApplication.getInstance().getActivity().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,    GROUP_PROJECTION, ContactsContract.Groups.TITLE+ "=?", new String[]{groupName}, ContactsContract.Groups.TITLE + " ASC");
        long group_id = 0;
        if(groupCursor.getCount() > 0){
            //Toast.makeText(UserApplication.getInstance().getActivity(), "Group is already available", Toast.LENGTH_SHORT).show();
            groupCursor.moveToFirst();
            group_id = groupCursor.getLong(groupCursor.getColumnIndex( ContactsContract.Groups._ID));
        }
        groupCursor.close();
        groupCursor = null;
        
        String[] cProjection = { ContactsContract.Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

        groupCursor = UserApplication.getInstance().getActivity().getContentResolver().query(Data.CONTENT_URI,
                cProjection,
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                new String[] { String.valueOf(group_id) }, null);
        if (groupCursor != null && groupCursor.moveToFirst())
        {
            do
            {

                int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

                String name = groupCursor.getString(nameCoumnIndex);
                P _contact = new P();
                _contact.name = name;
                //Toast.makeText(UserApplication.getInstance().getActivity(), name, Toast.LENGTH_SHORT).show();
                long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));
                _contact.contactId = contactId;
                Cursor numberCursor = UserApplication.getInstance().getActivity().getContentResolver().query(Phone.CONTENT_URI,
                        new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "=" + contactId, null, null);

                if (numberCursor.moveToFirst())
                {
                    int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
                    do
                    {
                        String phoneNumber = numberCursor.getString(numberColumnIndex);
                        _contact.number = phoneNumber;
                        //Toast.makeText(UserApplication.getInstance().getActivity(), phoneNumber, Toast.LENGTH_SHORT).show();
                    } while (numberCursor.moveToNext());
                    numberCursor.close();
                }
                
                Cursor emailCursor = UserApplication.getInstance().getActivity().getContentResolver().query(Email.CONTENT_URI,
                        new String[] { Email.ADDRESS }, Phone.CONTACT_ID + "=" + contactId, null, null);

                if (emailCursor.moveToFirst())
                {
                    int emailColumnIndex = emailCursor.getColumnIndex(Email.ADDRESS);
                    do
                    {
                        String email = emailCursor.getString(emailColumnIndex);
                        _contact.email = email;
                        //Toast.makeText(UserApplication.getInstance().getActivity(), phoneNumber, Toast.LENGTH_SHORT).show();
                    } while (emailCursor.moveToNext());
                    emailCursor.close();
                }
                contacts.add(_contact);
            } while (groupCursor.moveToNext());
            groupCursor.close();
        }
        return contacts;
	}
	
	public static void createCommunauteGroups()
	{
		for(String group: communaute_groups)
		{
			createContactGroup(group);
		}
	}
	
	public static long getContactGroupID(String groupName, Context context)
	{
		Cursor groupCursor = null;
        String[] GROUP_PROJECTION = new String[] { ContactsContract.Groups._ID,     ContactsContract.Groups.TITLE };
        groupCursor = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI,    GROUP_PROJECTION, ContactsContract.Groups.TITLE+ "=?", new String[]{groupName}, ContactsContract.Groups.TITLE + " ASC");
        long group_id = 0;
        if(groupCursor.getCount() > 0){
            //Toast.makeText(UserApplication.getInstance().getActivity(), "Group is already available", Toast.LENGTH_SHORT).show();
            groupCursor.moveToFirst();
            group_id = groupCursor.getLong(groupCursor.getColumnIndex( ContactsContract.Groups._ID));
        }
        groupCursor.close();
        groupCursor = null;
        return group_id;
	}
	
	public static Uri addContactToGroup(long personId, String groupName, Context context) {

		long group_id = getContactGroupID(groupName, context);
        
        if(group_id>0)
        {
		    ContentValues values = new ContentValues();
		    values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,personId);
		    values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,group_id);
		    values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
	
		    return context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }
        return null;
	}
	
	public static boolean removeContactFromGroup(long personId, String groupName, Context context) {
		long group_id = getContactGroupID(groupName, context);
        
        if(group_id>0)
        {
		    ContentValues values = new ContentValues();
		    
		    //values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,personId);
		    values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,0);
		    //values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
	
		    return context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID+"=? AND "+ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE+"=?", new String[]{String.valueOf(personId),ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE})>0;
        }
        return false;
	}
}

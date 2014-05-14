package it.eng.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SimpleFacebookSharePlugin extends CordovaPlugin {
	public static final String ACTION_SHARE = "share";

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		try {
			if (ACTION_SHARE.equals(action)) {
				JSONObject arg_object = args.getJSONObject(0);
				final String title = arg_object.getString("title");
				final String text = arg_object.getString("text");
				final String url = arg_object.getString("url");
				final String imageUrl = arg_object.getString("imageUrl");
				
				Log.i("SimpleFacebookSharePlugin", "title="+title);
				Log.i("SimpleFacebookSharePlugin", "text="+text);
				Log.i("SimpleFacebookSharePlugin", "url="+url);
				Log.i("SimpleFacebookSharePlugin", "imageUrl="+imageUrl);
				
				/*Intent theIntent = new Intent(Intent.ACTION_SEND);
				theIntent.putExtra(Intent.EXTRA_TEXT, text);
				theIntent.setType("text/plain");
				this.cordova.getActivity().startActivity(theIntent);*/
				
				final Activity activity = this.cordova.getActivity();
				
				// start Facebook Login
				Session.openActiveSession(activity, true, new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							shareContents(activity, title, text, url, imageUrl);
				        }
					}
			    });

				callbackContext.success();
				return true;
			}
			callbackContext.error("Invalid action");
			return false;
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		
		

	}
	
	private void shareContents(final Activity activity, final String title, final String text, final String url, final String imageUrl){
		Bundle params = new Bundle();
	    params.putString("name", title);
	    params.putString("caption", title);
	    params.putString("description", text);
	    params.putString("link", url);
	    params.putString("picture", imageUrl);

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this.cordova.getActivity(), Session.getActiveSession(),params))
	        .setOnCompleteListener(new OnCompleteListener() {
	        	
	            @Override
				public void onComplete(Bundle values, FacebookException error) {
					if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(activity,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(activity.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(activity, 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(activity, 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
				}

	        })
	        .build();
	    feedDialog.show();
	}
}

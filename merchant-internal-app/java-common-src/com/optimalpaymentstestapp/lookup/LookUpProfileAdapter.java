package com.optimalpaymentstestapp.lookup;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.optimalpaymentstestapp.R;
/**
 * @author Manisha.Rani
 * 
 */

public class LookUpProfileAdapter extends BaseAdapter {

	private static ArrayList<SearchResults> searchArrayList;
	private Context mContext;
	private RadioButton mCurrentlyCheckedRB;
	private String mCardLastDigits;
	private int mSelectedposition;
	public static String mCardId;
	public static String mPaymentToken;
	@SuppressWarnings("unused")
	private boolean isDefaultCardIndicator;
	public static boolean isRadioButtonChecked = false;

	public LookUpProfileAdapter(Context context,ArrayList<SearchResults> results, int i) {
		searchArrayList = results;
		mContext = context;
		this.setSelectedposition(i);
	}

	@Override
	public int getCount() {
		if (searchArrayList != null) {
			return searchArrayList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.lookupprofileitem, null);
		}
		try {
			final TextView cardNo = (TextView) convertView.findViewById(R.id.tv_cardNo);
			final TextView cardExp = (TextView) convertView.findViewById(R.id.tv_cardExp);
			final RadioButton radiobutton = (RadioButton) convertView.findViewById(R.id.rb_Choice);
			if (searchArrayList != null) {
				if (position < searchArrayList.size()) {
					
					getValuesOfCards(position,cardNo,cardExp);
					cardNo.setText("XXXX XXXX XXXX " + mCardLastDigits);
					cardExp.setText(searchArrayList.get(position).getMonth()+ "/"+ searchArrayList.get(position).getYear());
					
					boolean isDefaultCardIndicatorLocal = searchArrayList.get(position).isDefaultCardIndicator();
					if (isDefaultCardIndicatorLocal == true) {
						checkedradiobutton( position,cardNo,cardExp ,radiobutton );
					}else{
						 if(position == 0){
							 checkedradiobutton( position,cardNo,cardExp ,radiobutton );
						 }
					}
				}
			}
			radiobutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					getValuesOfCards(position,cardNo,cardExp);

					if (mCurrentlyCheckedRB == view)
						return;
					getValuesOfCards(position,cardNo,cardExp);
					mCurrentlyCheckedRB.setChecked(false);

					((RadioButton) view).setChecked(true);
					mCurrentlyCheckedRB = (RadioButton) view;
					if (mCurrentlyCheckedRB.isChecked()) {
						isRadioButtonChecked = true;
					} else {
						isRadioButtonChecked = false;
					}
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public int getSelectedposition() {
		return mSelectedposition;
	}

	public void setSelectedposition(int selectedposition) {
		this.mSelectedposition = selectedposition;
	}
     
	private void getValuesOfCards(	int position, TextView cardNo, TextView cardExp){
		mCardId = searchArrayList.get(position).getCardId();
		mCardLastDigits = searchArrayList.get(position).getLastDigits();
		mPaymentToken = searchArrayList.get(position).getPaymentToken();
		isDefaultCardIndicator = searchArrayList.get(position).isDefaultCardIndicator();
	}
	private void checkedradiobutton(int position,TextView cardNo,TextView cardExp ,RadioButton radiobutton ){
		getValuesOfCards(position,cardNo,cardExp);
		
		if (mCurrentlyCheckedRB == null)
			mCurrentlyCheckedRB = radiobutton;
		mCurrentlyCheckedRB.setChecked(true);
		if (mCurrentlyCheckedRB.isChecked()) {
			isRadioButtonChecked = true;
		}
		radiobutton.setChecked(true);
		isRadioButtonChecked = true;
	}
}

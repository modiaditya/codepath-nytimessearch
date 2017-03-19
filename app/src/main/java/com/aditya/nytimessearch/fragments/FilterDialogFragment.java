package com.aditya.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.nytimessearch.R;
import com.aditya.nytimessearch.models.Filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.aditya.nytimessearch.helpers.DateHelper.getMediumFormatDate;

/**
 * Created by amodi on 3/17/17.
 */

public class FilterDialogFragment extends DialogFragment {

    private static final String FILTER_EXTRA = "filter_extra";
    @BindView(R.id.tvBeginDate) TextView beginDate;
    @BindView(R.id.sSortOrder) Spinner sortOrder;
    @BindView(R.id.chkArts) CheckBox arts;
    @BindView(R.id.chkFashion) CheckBox fashion;
    @BindView(R.id.chkSports) CheckBox sports;
    @BindView(R.id.btnDismiss) Button dismiss;
    @BindView(R.id.btnSave) Button save;
    Date pickedBeginDate;
    FilterDialogListener filterDialogListener;
    Filter filter;

    public FilterDialogFragment() {
    }


    public static FilterDialogFragment newInstance(Filter filter) {
        Bundle args = new Bundle();
        args.putParcelable(FILTER_EXTRA, filter);
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.setArguments(args);
        return filterDialogFragment;
    }


    public interface FilterDialogListener {
        void onSave(Filter filter);
        void onDismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.filter = getArguments().getParcelable(FILTER_EXTRA);
        this.filterDialogListener = (FilterDialogListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_dialog, container);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getString(R.string.action_filter));
        // init spinner
        ArrayAdapter<CharSequence> simpleAdapter = ArrayAdapter.createFromResource(getContext(),
                                                                                   R.array.sort_order_array,
                                                                                   android.R.layout.simple_spinner_item);

        simpleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortOrder.setAdapter(simpleAdapter);


        beginDate.setText(getMediumFormatDate(getContext(), Calendar.getInstance().getTime()));
        beginDate.setOnClickListener(getBeginDateOnClickLister(beginDate, Calendar.getInstance()));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> newsDeskList = new ArrayList<>();
                if (arts.isChecked()) {
                    newsDeskList.add(Filter.NewsDeskType.ARTS.value);
                }
                if (fashion.isChecked()) {
                    newsDeskList.add(Filter.NewsDeskType.FASHION_N_STYLE.value);
                }
                if (sports.isChecked()) {
                    newsDeskList.add(Filter.NewsDeskType.SPORTS.value);
                }

                Filter filter = new Filter(pickedBeginDate,
                                           (Filter.SortOrder.values()[sortOrder.getSelectedItemPosition()]).name(),
                                           newsDeskList);

                filterDialogListener.onSave(filter);
                dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialogListener.onDismiss();
            }
        });


    }

    private View.OnClickListener getBeginDateOnClickLister(final TextView beginDate, final Calendar c) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                DatePickerDialog datePickerDialog =
                    new DatePickerDialog(getContext(),
                                         new DatePickerDialog.OnDateSetListener() {

                                             @Override
                                             public void onDateSet(DatePicker view, int yearOfDate,
                                                                   int monthOfYear, int dayOfMonth) {

                                                 Calendar c = Calendar.getInstance();
                                                 c.set(yearOfDate, monthOfYear, dayOfMonth);
                                                 pickedBeginDate = c.getTime();
                                                 beginDate.setText(getMediumFormatDate(getContext(), pickedBeginDate));

                                             }
                                         }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                datePickerDialog.show();
            }
        };
    }
}

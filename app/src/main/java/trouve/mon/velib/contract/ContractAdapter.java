package trouve.mon.velib.contract;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import trouve.mon.velib.R;


public class ContractAdapter extends ArrayAdapter<Contract> {

    Context context;
    int resource;
    List<Contract> contracts;

    int selectedItemPosition = -1;


    public ContractAdapter(Context context, int resource, List<Contract> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.contracts = objects;
    }

    public void selectItem(int i) {
        selectedItemPosition = i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContractHolder holder;
        View row = convertView;
        Contract contract = contracts.get(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new ContractHolder();
            holder.contrat = (TextView) row.findViewById(R.id.contract_name);
            holder.service = (TextView) row.findViewById(R.id.service_name);
            holder.flag = (ImageView) row.findViewById(R.id.country_flag);
            row.setTag(holder);
        } else {
            holder = (ContractHolder) row.getTag();
        }

        holder.contrat.setText(contract.getName());
        holder.service.setText(contract.getServiceName());
        String country = contract.getCountry();
        if (country != null) {
            int res = Country.getDrawableResourceId(country);
            if (res != -1) {
                holder.flag.setImageResource(res);
            } else {
                holder.flag.setImageResource(android.R.color.transparent);
            }
        }

        if (position == selectedItemPosition) {
            row.setBackgroundColor(context.getResources().getColor(R.color.logo_blue_light));
            holder.contrat.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            row.setBackgroundColor(Color.TRANSPARENT);
            holder.contrat.setTextColor(context.getResources().getColor(R.color.logo_blue));
        }

        return row;
    }

    static class ContractHolder {
        TextView contrat;
        TextView service;
        ImageView flag;
    }
}

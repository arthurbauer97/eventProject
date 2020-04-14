package com.arthur.eventsapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arthur.eventsapp.R;
import com.arthur.eventsapp.data.domain.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FilterEventsAdapter extends BaseAdapter {

    //Itens de exibição / filtrados
    private List<Event> itens_exibicao;
    //Essa lista contem todos os itens.
    private List<Event> itens;
    //Utilizado no getView para carregar e construir um item.
    private LayoutInflater layoutInflater;
    //Item clicado / click
    private ItemClickListener mItemClickListener;

    public FilterEventsAdapter(Context context, List<Event> itens) {
        this.itens = itens;
        this.itens_exibicao = itens;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens_exibicao.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itens_exibicao.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return itens_exibicao.get(arg0).getId();
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        ItemHelper itemHelper = new ItemHelper();
        Event objeto = itens_exibicao.get(position);

        if (arg1 == null) {
            arg1 = layoutInflater.inflate(R.layout.item_adapter_events, null);
            itemHelper.nameEvent = arg1.findViewById(R.id.tv_nameEvent);
            itemHelper.cityEvent = arg1.findViewById(R.id.tv_cityEvent);
            itemHelper.localEvent = arg1.findViewById(R.id.tv_localEvent);
            itemHelper.photoEvent = arg1.findViewById(R.id.img_photoEvent);
            arg1.setTag(itemHelper);
        } else {
            itemHelper = (ItemHelper) arg1.getTag();
        }
        itemHelper.photoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(objeto);
                }
            }
        });

        Picasso
                .get()
                .load(objeto.getPhoto())
                .into(itemHelper.photoEvent);
        itemHelper.nameEvent.setText(objeto.getName());
        itemHelper.cityEvent.setText(objeto.getCity());
        itemHelper.localEvent.setText(objeto.getLocalName());


        return arg1;
    }

    private class ItemHelper {
        TextView cityEvent;
        TextView localEvent;
        ImageView photoEvent;
        TextView nameEvent;
    }

    /** Método responsável pelo filtro. Utilizaremos em um EditText
     *
     * @return
     */
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = itens.size();
                    results.values = itens;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Event> itens_filtrados = new ArrayList<Event>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < itens.size(); i++) {
                        Event data = itens.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getName().toLowerCase();
                        String condicao2 = data.getLocalName().toLowerCase();
                        String condicao3 = data.getCity().toLowerCase();

                        if (condicao.contains(filtro) || condicao2.contains(filtro) || condicao3.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                itens_exibicao = (List<Event>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(Event event);
    }

}
package com.arthur.eventsapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arthur.eventsapp.R
import com.arthur.eventsapp.data.domain.Event
import com.arthur.eventsapp.view.activities.DetailsActivity
import com.arthur.eventsapp.view.adapters.FilterEventsAdapter
import com.arthur.eventsapp.viewmodel.AllViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_events.view.*


class EventsFragment : Fragment(),
    FilterEventsAdapter.ItemClickListener{

    lateinit var eventsAdapter : FilterEventsAdapter
    lateinit var viewModel: AllViewModel
    lateinit var listAllEvents : List<Event>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        viewModel = ViewModelProviders.of(this).get(AllViewModel::class.java)


        viewModel
            .getAllEvents()
            .observe(this, Observer {
                if (it.data == null) {
                    Snackbar.make(
                        events,
                        it.error.toString(),
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    eventsAdapter = FilterEventsAdapter(context!!,it.data)
                    view.listview.setAdapter(eventsAdapter)
                    eventsAdapter.addItemClickListener(this)
                }
            })


        view.til_filter.addTextChangedListener(
            object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    eventsAdapter.filter.filter(s.toString())
                }
            }
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //Método responsável por carregar a listagem de Influciadores na View.
    private fun loadEvents() {

    }

    override fun onItemClick(event:Event) {
        startActivity(
            Intent(
                context,
                DetailsActivity::class.java
            )
                .putExtra("nomeEvento", event.name)
                .putExtra("cidadeEvento",event.city)
                .putExtra("descricaoEvento",event.description)
                .putExtra("localEvento",event.localName)
                .putExtra("photoEvento",event.photo)
                .putExtra("dataEvento",event.date)
                .putExtra("latEvento",event.coordinates.latitude)
                .putExtra("lonEvento",event.coordinates.longitude)
        )
    }
}

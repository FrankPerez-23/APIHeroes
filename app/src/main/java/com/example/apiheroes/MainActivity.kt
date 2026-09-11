package com.example.apiheroes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiheroes.model.HeroApiResponse
import com.example.apiheroes.viewmodel.HeroViewModel

class MainActivity : AppCompatActivity() {

    private val heroViewModel: HeroViewModel by viewModels()

    private var listaOriginalHeroes: List<HeroApiResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val rvHeroes = findViewById<RecyclerView>(R.id.rvHeroes)
        val adapter = SimpleHeroAdapter { heroeSeleccionado ->
            // Crea el Intent para abrir la nueva pantalla
            val intent = Intent(this, DatosHeroActivity::class.java).apply {
                // Pasa los datos que necesitarás en la otra ventana
                putExtra("EXTRA_HERO", heroeSeleccionado)
            }
            startActivity(intent)
        }

        // Cuadrícula de 3 columnas automáticas con scroll:
        rvHeroes.layoutManager = GridLayoutManager(this, 3)
        rvHeroes.adapter = adapter

        // Observa la lista completa que entrega getAll()
        heroViewModel.Allheroes.observe(this) { heroes ->
            if (heroes != null) {
                listaOriginalHeroes = heroes
                adapter.updateList(heroes)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val texto = newText.orEmpty().lowercase().trim()

                // Filtramos sobre la lista original comparando con 'name'
                val listaFiltrada = if (texto.isEmpty()) {
                    listaOriginalHeroes
                } else {
                    listaOriginalHeroes.filter { heroe ->
                        heroe.name?.lowercase()?.contains(texto) == true
                    }
                }

                // Informamos al adaptador para que actualice las tarjetas visibles
                adapter.updateList(listaFiltrada)
                return true
            }
        })

        // Llamar a la API al abrir la pantalla
        heroViewModel.obtenerTodosHeroes()
    }

    class SimpleHeroAdapter(

        private var list: List<HeroApiResponse> = emptyList(),
        private val onHeroClick: (HeroApiResponse) -> Unit

        ) : RecyclerView.Adapter<SimpleHeroAdapter.VH>() {

        // 1. Contenedor de la vista (guarda la tarjeta en memoria)
        class VH(vistaIndividual: View) : RecyclerView.ViewHolder(vistaIndividual)

        // 2. CREAR: Infla el archivo XML para armar una tarjeta nueva
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_heroe, parent, false)
            return VH(vista)
        }

        // 3. CONTAR: Informa cuántos elementos hay para mostrar
        override fun getItemCount(): Int = list.size

        // 4. VINCULAR: Rellena la tarjeta con la información del héroe actual
        override fun onBindViewHolder(holder: VH, position: Int) {
            val heroeActual = list[position]

            // Asignar el nombre del héroe al texto
            holder.itemView.findViewById<TextView>(R.id.tvHeroName).text = heroeActual.name

            // Cargar la imagen del héroe con Glide
            Glide.with(holder.itemView.context)
                .load(heroeActual.images?.sm ?: heroeActual.images?.md)
                .centerCrop()
                .into(holder.itemView.findViewById<ImageView>(R.id.ivHero)
                )

            holder.itemView.setOnClickListener {
                onHeroClick(heroeActual)
            }
        }

        // 5. ACTUALIZAR: Recibe la nueva lista (de la API o del SearchView) y repinta
        fun updateList(nuevaLista: List<HeroApiResponse>) {
            list = nuevaLista
            notifyDataSetChanged() // Refresca la interfaz
        }
    }
}
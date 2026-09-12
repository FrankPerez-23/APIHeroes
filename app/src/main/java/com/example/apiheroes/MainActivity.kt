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
            val intent = Intent(this, DatosHeroActivity::class.java).apply {
                putExtra("EXTRA_HERO", heroeSeleccionado)
            }
            startActivity(intent)
        }

        rvHeroes.layoutManager = GridLayoutManager(this, 3)
        rvHeroes.adapter = adapter

        heroViewModel.Allheroes.observe(this) { heroes ->
            if (heroes != null) {
                listaOriginalHeroes = heroes
                adapter.updateList(heroes)
            }
        }

        // Se encarga de avisar cuando el usuario interactue con el buscador
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            } // Se encarga de realizar una accion cuando el usuario presiona la lupa del buscador o presiona enter, pero en este caso como lo hacemos en tiempo real no es necesario q el usuario presione enter para buscar por lo q retorna falso para no ejecutar nada

            override fun onQueryTextChange(newText: String?): Boolean { // Se encarga de que con cada letra presionada del teclado se actualiza el buscador y el newText es el String q esta en el buscador en esa ultima actualizacion
                val texto = newText.orEmpty().lowercase().trim() // Se encarga de modificar el string para evitar errores
                // orEmpty() se encarga de tranformar en String vacio ("") si es que la variable esta vacia
                // lowercase() se encarga de convertir en minuscula toda la cadena de Texto
                // trim() se encarga de quitar el espacio del comienzo y final de la cadena de texto

                val listaFiltrada = if (texto.isEmpty()) {
                    listaOriginalHeroes // con una condicion se verifica que la variable texto este vacia, si es asi entonces toda la lista de heroes se guardará en listaFiltrada
                } else { // Caso contrario
                    listaOriginalHeroes.filter { heroe ->
                        heroe.name?.contains(texto, ignoreCase = true) ?: false
                    } // Texto no esta vacio entonces usamos filter para buscar todos los heroes que tengan la cadena de texto de la variable texto dentro de su nombre, con contains se hace la comparacion de texto con los nombres de los heroes y  con ignoreCase = true hacemos que el sistema ignore las mayusculas y minusculas de la cadena de texto, luego con ?: hacemos que si el resultado es null nos devuelva false, esto porq filter necesita de un valor boolean
                }

                // Informamos al adaptador para que actualice las tarjetas visibles
                adapter.updateList(listaFiltrada)
                return true
            }
        })

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
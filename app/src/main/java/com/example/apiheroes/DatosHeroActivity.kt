package com.example.apiheroes

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.example.apiheroes.model.HeroApiResponse
import com.google.android.material.progressindicator.LinearProgressIndicator

class DatosHeroActivity : AppCompatActivity() {

    private lateinit var imgHero: ImageView
    private lateinit var tvHero: TextView
    private lateinit var tvNombreHero: TextView
    private lateinit var btnVolver: Button
    private lateinit var tvBando: TextView
    private lateinit var tvEditorial: TextView
    private lateinit var tvPoder: TextView
    private lateinit var tvLugarNacimiento: TextView
    private lateinit var tvBase: TextView
    private lateinit var tvValorInteligencia: TextView
    private lateinit var tvValorFuerza: TextView
    private lateinit var tvValorVelocidad: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_hero)

        val datosHeroe = IntentCompat.getSerializableExtra(intent, "EXTRA_HERO", HeroApiResponse::class.java)

        val progressIntelligence = findViewById<LinearProgressIndicator>(R.id.progressIntelligence)
        val progressStrength = findViewById<LinearProgressIndicator>(R.id.progressStrength)
        val progressSpeed = findViewById<LinearProgressIndicator>(R.id.progressSpeed)

        imgHero = findViewById(R.id.imgHero)
        tvHero = findViewById(R.id.tvHero)
        tvNombreHero = findViewById(R.id.tvNombreHero)
        btnVolver = findViewById(R.id.btnVolver)
        tvBando = findViewById(R.id.tvBando)
        tvEditorial = findViewById(R.id.tvEditorial)
        tvPoder = findViewById(R.id.tvPoder)
        tvLugarNacimiento = findViewById(R.id.tvLugarNacimiento)
        tvBase = findViewById(R.id.tvBase)
        tvValorInteligencia = findViewById(R.id.tvValorInteligencia)
        tvValorFuerza = findViewById(R.id.tvValorFuerza)
        tvValorVelocidad = findViewById(R.id.tvValorVelocidad)

        Glide.with(this)
            .load(datosHeroe?.images?.lg ?: datosHeroe?.images?.md)
            .centerCrop()
            .into(imgHero)

        if (datosHeroe?.biography?.alignment == "good"){
            tvBando.text = "Bando: Héroe"
            tvBando.setTextColor(Color.parseColor("#4CAF50"))
        }else if ((datosHeroe?.biography?.alignment == "bad")){
            tvBando.text = "Bando: Villano"
            tvBando.setTextColor(Color.parseColor("#F44336"))
        }else{
            tvBando.text = "Bando: Neutral"
        }

        val poderTotal = (datosHeroe?.powerstats?.power ?: 0) + (datosHeroe?.powerstats?.combat ?: 0)

        if (poderTotal > 150){
            tvPoder.text = "Rango: Super Poderoso"
        }else{
            tvPoder.text = "Rango: Estandar"
        }

        if (datosHeroe?.biography?.placeOfBirth == "-"){
            tvLugarNacimiento.text = "Lugar de Nacimiento: Desconocido"
        }else{
            tvLugarNacimiento.text = "Lugar de Nacimiento: ${datosHeroe?.biography?.placeOfBirth ?: "Lugar de Nacimiento: Desconocido"}"
        }

        if (datosHeroe?.work?.base == "-"){
            tvBase.text = "Base: Desconocido"
        }else{
            tvBase.text = "Base: ${datosHeroe?.work?.base ?: "Base: Desconocido"}"
        }

        val inteligencia = datosHeroe?.powerstats?.intelligence ?: 0
        val fuerza = datosHeroe?.powerstats?.strength ?: 0
        val velocidad = datosHeroe?.powerstats?.speed ?: 0

        tvHero.text = datosHeroe?.name
        tvNombreHero.text = "Nombre Real: ${datosHeroe?.biography?.fullName ?.ifBlank { "Desconocido" } ?: "Nombre Real: Desconocido"}"
        tvValorInteligencia.text = "$inteligencia"
        tvValorFuerza.text = "$fuerza"
        tvValorVelocidad.text = "$velocidad"
        progressIntelligence.setProgressCompat(inteligencia, true)
        progressStrength.setProgressCompat(fuerza, true)
        progressSpeed.setProgressCompat(velocidad, true)

        tvEditorial.text = "Casa Editorial: ${datosHeroe?.biography?.publisher ?: "Casa Editorial: Se desconoce"}"


        btnVolver.setOnClickListener {
            finish()
        }
    }
}
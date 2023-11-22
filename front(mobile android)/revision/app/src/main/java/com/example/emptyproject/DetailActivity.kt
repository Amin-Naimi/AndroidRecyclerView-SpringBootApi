package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    lateinit var offreIdView : TextView
    lateinit var  offreIntutileView: TextView
    lateinit var  offreSpecialiterView: TextView
    lateinit var offreSociteView : TextView
    lateinit var offreNbpostesView: TextView
    lateinit var offrePaysView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val offreIdText=intent.getStringExtra("id")
        val offreIntutileText=intent.getStringExtra("intutile")
        val offreSpecialiterText=intent.getStringExtra("specialiter")
        val offreSociteText=intent.getStringExtra("socite")
        val offreNbpostesText=intent.getStringExtra("nbpostes")
        val offrePaysViewText=intent.getStringExtra("pays")


        offreIdView=findViewById(R.id.id_offre)
        offreIntutileView=findViewById(R.id.intutile_offre)
        offreSpecialiterView=findViewById(R.id.specialiter_offre)
        offreSociteView=findViewById(R.id.socite_offre)
        offreNbpostesView=findViewById(R.id.nbpostes_offre)
        offrePaysView=findViewById(R.id.pays_offre)


        offreIdView.text = offreIdText
        offreIntutileView.text = offreIntutileText
        offreSpecialiterView.text = offreSpecialiterText
        offreSociteView.text = offreSociteText
        offreNbpostesView.text = offreNbpostesText
        offrePaysView.text = offrePaysViewText



    }
}
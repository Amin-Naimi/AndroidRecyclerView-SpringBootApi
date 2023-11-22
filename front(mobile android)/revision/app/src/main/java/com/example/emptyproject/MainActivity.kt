package com.example.emptyproject

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emptyproject.apiservice.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ItemClickListener {

    lateinit var recyclerView: RecyclerView

    lateinit var deleteBtn: FloatingActionButton
    lateinit var addBtn: FloatingActionButton
    lateinit var showBtn: FloatingActionButton
    lateinit var updateBtn: FloatingActionButton

    private var offreListe: MutableList<Offre>? = null
    private lateinit var adapter: MyAdapter

    val scope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        deleteBtn = findViewById(R.id.delete)
        addBtn = findViewById(R.id.add)
        showBtn = findViewById(R.id.show)
        updateBtn = findViewById(R.id.update)

        // Views
        recyclerView = findViewById(R.id.recyclerview)

        // Get All offre
        scope.launch {
            // Data liste
            offreListe = initialiserOffreListe().toMutableList()
            // Adapter configuration
            adapter = MyAdapter(offreListe!!, this@MainActivity)
            val layoutRecycler = LinearLayoutManager(applicationContext)
            recyclerView.layoutManager = layoutRecycler
            recyclerView.adapter = adapter
        }

        // Add new offre
        addBtn.setOnClickListener {
            afficherDialogueAddOffre()
        }
        // Show offre details
        showBtn.setOnClickListener {
            afficherOffreDetail()
        }
        // Delete offre

        deleteBtn.setOnClickListener {
            scope.launch {
                deleteOffre()
            }
        }

        // Update offre
        updateBtn.setOnClickListener {
                updateOffre()
        }


    }


    // initialisation des offre les offre

    suspend fun initialiserOffreListe(): List<Offre> {
        try {
            val response = ApiClient.apiService.getOffres()

            if (response.isSuccessful && response.body() != null) {
                return response.body() as ArrayList<Offre>
            } else {
                Log.e("Error", response.message())
                throw RuntimeException("Échec de la requête pour obtenir les offres")
            }
        } catch (e: Exception) {
            Log.e(
                "Error",
                "Une erreur s'est produite lors de l'initialisation de la liste d'offres: ${e.message}"
            )
            throw RuntimeException("Erreur lors de l'initialisation de la liste d'offres", e)
        }
    }


    // Add offre :
    private fun afficherDialogueAddOffre() {
        val layout = LayoutInflater.from(this)
        val dialog = layout.inflate(R.layout.edittext, null)

        val intutileText = dialog.findViewById<EditText>(R.id.offre_intutile)
        val specialiterText = dialog.findViewById<EditText>(R.id.offre_specialiter)
        val sociteText = dialog.findViewById<EditText>(R.id.offre_socite)
        val nbpostesText = dialog.findViewById<EditText>(R.id.offre_nbpostes)
        val paysText = dialog.findViewById<EditText>(R.id.offre_pays)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ajouter une nouvel offre")
        builder.setView(dialog)
        builder.setPositiveButton("Ajouter",
            DialogInterface.OnClickListener { dialogInterface, i ->

                val intutile = intutileText.text.toString()
                val specialiter = specialiterText.text.toString()
                val socite = sociteText.text.toString()
                val nbpostes = nbpostesText.text.toString().toInt()
                val pays = paysText.text.toString()
                scope.launch {
                    ajouterOffre(intutile, specialiter, socite, nbpostes, pays)
                }
            })
        builder.setNegativeButton("Annuler", null)
        builder.show()
    }

    suspend fun ajouterOffre(
        intutile: String,
        specialiter: String,
        socite: String,
        nbpostes: Int,
        pays: String
    ) {
        val newOffre = Offre(
            intutile = intutile,
            specialiter = specialiter,
            socite = socite,
            nbpostes = nbpostes,
            pays = pays
        )
        try {
            val response = ApiClient.apiService.posteOffre(newOffre)
            if (response.isSuccessful && response.body() != null) {
                Log.i("Success", response.body().toString())
                val addedOffre =
                    response.body()
                Log.i("Success", addedOffre.toString())
                offreListe?.add(addedOffre!!)
                adapter.notifyDataSetChanged()
            } else {
                Log.e("Error", response.message().toString())
            }
        } catch (e: Exception) {

            Log.e("Error", e.message.toString())
        }
    }

    // Afficher offre detaile
    private fun afficherOffreDetail() {

        if (adapter.selectedItemPosition != RecyclerView.NO_POSITION) {

            val selectedOffre = offreListe?.get(adapter.selectedItemPosition)

            if (selectedOffre != null) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("id", selectedOffre.id)
                intent.putExtra("intutile", selectedOffre.intutile)
                intent.putExtra("specialiter", selectedOffre.specialiter)
                intent.putExtra("socite", selectedOffre.socite)
                intent.putExtra("nbpostes", selectedOffre.nbpostes.toString())
                intent.putExtra("pays", selectedOffre.pays)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Sélectionnez une offre avant de cliquer sur Afficher",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Sélectionnez une offre avant de cliquer sur Afficher",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // DELete offre

    suspend fun deleteOffre() {
        var index = -1
        if (adapter.selectedItemPosition != RecyclerView.NO_POSITION) {
            val selectedOffre = offreListe?.get(adapter.selectedItemPosition)
            if (selectedOffre != null) {
                var offreId = selectedOffre.id
                try {
                    val response = ApiClient.apiService.deleteOffre(offreId!!)
                    if (response) {
                        index = offreListe!!.indexOf(selectedOffre)
                        offreListe!!.removeAt(index)
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {

                    Log.e("Error", e.message.toString())
                }

            } else {
                Toast.makeText(
                    this,
                    "Sélectionnez une offre avant de cliquer sur delete",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Sélectionnez une offre avant de cliquer sur delete",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d("DELETE", "selectedItemPosition: ${adapter.selectedItemPosition}, index: $index")
    }

    // Update Offre


    private fun updateOffre() {
        if (adapter.selectedItemPosition != RecyclerView.NO_POSITION) {
            var selectedOffre = offreListe?.get(adapter.selectedItemPosition)

            if (selectedOffre != null) {

                val layoutInflater = LayoutInflater.from(this)
                val dialogView = layoutInflater.inflate(R.layout.edittext, null)

                val intutileText = dialogView.findViewById<EditText>(R.id.offre_intutile)
                val specialiterText = dialogView.findViewById<EditText>(R.id.offre_specialiter)
                val sociteText = dialogView.findViewById<EditText>(R.id.offre_socite)
                val nbpostesText = dialogView.findViewById<EditText>(R.id.offre_nbpostes)
                val paysText = dialogView.findViewById<EditText>(R.id.offre_pays)

                intutileText.setText(selectedOffre.intutile)
                specialiterText.setText(selectedOffre.specialiter)
                sociteText.setText(selectedOffre.socite)
                nbpostesText.setText(selectedOffre.nbpostes.toString())
                paysText.setText(selectedOffre.pays)

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Mettre à jour l'offre")
                builder.setView(dialogView)
                builder.setPositiveButton("Mettre à jour", DialogInterface.OnClickListener { dialogInterface, i ->

                    var updatedOffre = Offre(
                        intutile = intutileText.text.toString(),
                        specialiter = specialiterText.text.toString(),
                        socite = sociteText.text.toString(),
                        nbpostes = nbpostesText.text.toString().toInt(),
                        pays = paysText.text.toString()
                    )
                    val offerId = selectedOffre!!.id

                    // Utilisez une coroutine pour effectuer la mise à jour
                    scope.launch {
                        try {
                            val response = ApiClient.apiService.updateOffre(
                                offerId!!,
                                updatedOffre
                            )
                            if (response.isSuccessful && response.body() != null) {

                                 updatedOffre.id = selectedOffre.id
                                offreListe?.set(adapter.selectedItemPosition, updatedOffre)
                                adapter.notifyItemChanged(adapter.selectedItemPosition)

                                Log.i("Success", response.body().toString())
                            } else {
                                Log.e("Error", response.message())
                            }
                        } catch (e: Exception) {
                            Log.e("Error", e.message.toString())
                        }
                    }
                })
                builder.setNegativeButton("Annuler", null)
                builder.show()
            }
        } else {
            Toast.makeText(this, "Sélectionnez une offre avant de cliquer sur Mettre à jour", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onitemClicked(v: View, item: Offre) {
        val viewHolder = recyclerView.getChildViewHolder(v)
        val position = viewHolder.adapterPosition

        if (position != RecyclerView.NO_POSITION) {
            adapter.selectedItemPosition = position
            Toast.makeText(this, "Élément cliqué", Toast.LENGTH_LONG).show()
            adapter.notifyDataSetChanged()
        }
    }

}

/** Coroutine **/
/*
En Kotlin, les coroutines sont un moyen de gérer la concurrence de manière plus légère et
expressive que les threads traditionnels. Elles permettent d'écrire du code asynchrone de manière
séquentielle, facilitant la gestion des tâches concurrentes sans les complexités associées aux threads classiques.
*
*/

/** Suspend **/

/*
*Le mot-clé suspend est utilisé pour déclarer des fonctions qui peuvent être mises en pause et reprises
ultérieurement. Ces fonctions sont souvent utilisées avec les coroutines pour indiquer qu'elles peuvent
effectuer des opérations asynchrones sans bloquer le fil d'exécution

*/

/** coroutineScope **/
/*
coroutineScope : Une fonction de portée de coroutine qui crée une portée de coroutine.
Les coroutines lancées à l'intérieur de cette portée peuvent être gérées plus facilement et de manière plus structurée.
*/
/** scope.launch **/
/*
* En Kotlin, scope.launch est utilisé pour démarrer une coroutine.
Une coroutine est une séquence d'instructions pouvant être exécutées de manière asynchrone.
* Le scope fait référence à une portée de coroutine, qui définit le contexte dans lequel la coroutine s'exécute.*/
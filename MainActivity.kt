package com.wapp.weatherapp
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
class MainActivity : AppCompatActivity() {
    val CITY: String = "Timisoara,RO"
    val API: String = "3d3d6e6313dedc8b91a9e0a3fc1c9d46"
    override fun onCreate(savedInstanceState: Bundle?) {//Life cycle hook apelat o singura data la creare
        super.onCreate(savedInstanceState)//Bundle-ul se creaza
        setContentView(R.layout.activity_main)//Se seteaza activit_main-ul(layout se reseteaza)
        weatherTask().execute()
    }
    inner class weatherTask() : AsyncTask<String, Void, String>() {//Are un onPreExecute() care isi seteaza toate view-urile
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }
        override fun doInBackground(vararg params: String?): String? {// face un request care are loc in background
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8)
            }catch (e: Exception){
                response = null// daca nu gaseste nimic pune null
            }
            return response
        }
        override fun onPostExecute(result: String?) {// onPostExecute dupce ce face request-ul destructureaza json-ul
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")

                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")

                val pressure = main.getString("pressure")

                val sunrise:Long = sys.getLong("sunrise")

                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")


                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}

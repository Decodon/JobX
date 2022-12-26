package ie.wit.jobx.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize


@IgnoreExtraProperties
@Parcelize
data class JobModel(var uid: String? = "",
                    var title: String = "",
                    var description: String = "",
                    var date: String = "",
                    var image: Uri = Uri.EMPTY,
                    var lat : Double = 0.00,
                    var lng: Double = 0.00,
                    var net: Double = 0.00,
                    var vat: Double = 0.00,
                    var gross: Double = 0.00,
                    var zoom: Float = 0f,
                    var email: String? = "joe@bloggs.com") : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "date" to date,
            "net" to net,
            "vat" to vat,
            "gross" to gross,
            "email" to email
        )
    }
}

///**
// * Location data class
// */
//@Parcelize
//data class Location(var lat: Double = 0.0,
//                    var lng: Double = 0.0,
//                    var zoom: Float = 0f) : Parcelable
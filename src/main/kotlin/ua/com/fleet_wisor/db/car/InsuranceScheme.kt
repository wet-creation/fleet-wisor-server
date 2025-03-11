package ua.com.fleet_wisor.db.car

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import ua.com.fleet_wisor.models.car.Insurance

object InsuranceTable: Table<Nothing>("insurance") {
    val id = int("id").primaryKey()
    val carId = int("carId")
    var startDate = date("startDate")
    var endDate = date("endDate")
    var photoUrl = varchar("photoUrl")
}


fun QueryRowSet.toInsurance(): Insurance {
    val t = this
    return Insurance(
        id = t[InsuranceTable.id]!!,
        startDate = t[InsuranceTable.startDate].toString(),
        endDate = t[InsuranceTable.endDate].toString(),
        photoUrl = t[InsuranceTable.photoUrl]!!,
        car = toCar()
    )
}



package events.employee

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import events.Event
import java.math.BigDecimal

@JsonTypeName("position-created")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class PositionCreated(
    val positionId: String,
    val title: String,
    val minHourlyWage: BigDecimal,
    val maxHourlyWage: BigDecimal
): Event() {

    init {
        type = "Position created"
    }

}

@JsonTypeName("position-title-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class PositionTitleChanged(val title: String): Event() {

    init {
        type = "Position title changed"
    }

}

@JsonTypeName("position-wage-range-changed")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class PositionWageRangeChanged(val minHourlyWage: BigDecimal, val maxHourlyWage: BigDecimal): Event() {

    init {
        type = "Position wage range changed"
    }

}


@JsonTypeName("position-deleted")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class PositionDeleted(val deleted: Boolean = true): Event() {

    init {
        type = "Position deleted"
    }

}
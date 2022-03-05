# Hostfully Technical Test

##The user can create a booking and a block. Bookings can be canceled and rebooked. Blocks can be created and deleted.


##Terminology

###A booking is when a guest selects a start and end date and submits a reservation on a property.

###A block is when the property owner selects a start and end date where no one can make a booking on the dates within the date range.


#Making a Reservation
In order to prevent double (overlapping) bookings, we need to lock the property row before checking for its availability. So we lock the related row of "Property" table using the Lock annotation of Spring Data.



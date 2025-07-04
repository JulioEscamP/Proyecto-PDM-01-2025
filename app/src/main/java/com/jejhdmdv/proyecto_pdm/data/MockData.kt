package com.jejhdmdv.proyecto_pdm.data

import com.jejhdmdv.proyecto_pdm.model.admin.Appointment
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatus
import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.model.admin.Product

/**
 * Datos de prueba para el desarrollo y testing del panel de administrador
 * Estos datos serán reemplazados por datos reales del backend
 */
object MockData {
    
    //lista de prueba
    val mockProducts = listOf(
        Product(
            id = "1",
            name = "Comida Premium para Perros",
            price = 45.99,
            category = "Alimentación",
            description = "Comida balanceada premium para perros adultos. Rica en proteínas y vitaminas esenciales.",
            imageUrl = "https://example.com/dog-food.jpg",
            isActive = true,
            createdAt = "2024-01-15",
            updatedAt = "2024-01-15"
        ),
        Product(
            id = "2",
            name = "Juguete Interactivo para Gatos",
            price = 15.50,
            category = "Juguetes",
            description = "Juguete interactivo que estimula la mente de tu gato y lo mantiene activo.",
            imageUrl = "https://example.com/cat-toy.jpg",
            isActive = true,
            createdAt = "2024-01-16",
            updatedAt = "2024-01-16"
        ),
        Product(
            id = "3",
            name = "Collar Antipulgas",
            price = 25.00,
            category = "Salud",
            description = "Collar antipulgas de larga duración, protege a tu mascota por hasta 8 meses.",
            imageUrl = "https://example.com/flea-collar.jpg",
            isActive = true,
            createdAt = "2024-01-17",
            updatedAt = "2024-01-17"
        ),
        Product(
            id = "4",
            name = "Cama Ortopédica",
            price = 89.99,
            category = "Accesorios",
            description = "Cama ortopédica con memoria de forma, ideal para mascotas mayores.",
            imageUrl = "https://example.com/orthopedic-bed.jpg",
            isActive = false,
            createdAt = "2024-01-18",
            updatedAt = "2024-01-18"
        )
    )
    
    //clinicas veterianrias de prueba
    val mockClinics = listOf(
        Clinic(
            id = "1",
            name = "Clínica Veterinaria San José",
            phoneNumber = "+506 2222-3333",
            address = "Avenida Central, San José, Costa Rica",
            schedule = "Lunes a Viernes: 8:00 AM - 6:00 PM\nSábados: 8:00 AM - 2:00 PM",
            isActive = true,
            createdAt = "2024-01-10",
            updatedAt = "2024-01-10"
        ),
        Clinic(
            id = "2",
            name = "Hospital Veterinario 24 Horas",
            phoneNumber = "+506 2555-7777",
            address = "Escazú, San José, Costa Rica",
            schedule = "24 horas, todos los días",
            isActive = true,
            createdAt = "2024-01-11",
            updatedAt = "2024-01-11"
        ),
        Clinic(
            id = "3",
            name = "Clínica Veterinaria Cartago",
            phoneNumber = "+506 2591-8888",
            address = "Centro de Cartago, Cartago, Costa Rica",
            schedule = "Lunes a Viernes: 7:00 AM - 5:00 PM\nSábados: 7:00 AM - 12:00 PM",
            isActive = true,
            createdAt = "2024-01-12",
            updatedAt = "2024-01-12"
        ),
        Clinic(
            id = "4",
            name = "Veterinaria Heredia",
            phoneNumber = "+506 2260-4444",
            address = "Heredia Centro, Heredia, Costa Rica",
            schedule = "Lunes a Sábado: 8:00 AM - 6:00 PM",
            isActive = false,
            createdAt = "2024-01-13",
            updatedAt = "2024-01-13"
        )
    )
    
    //lista de citas de prueba
    val mockAppointments = listOf(
        Appointment(
            id = "1",
            userId = "user1",
            userName = "María González",
            userEmail = "maria@example.com",
            petName = "Max",
            petType = "Perro",
            appointmentDate = "2024-02-15",
            appointmentTime = "10:00 AM",
            reason = "Vacunación anual",
            status = AppointmentStatus.PENDING,
            adminNotes = "",
            createdAt = "2024-02-01",
            updatedAt = "2024-02-01"
        ),
        Appointment(
            id = "2",
            userId = "user2",
            userName = "Carlos Rodríguez",
            userEmail = "carlos@example.com",
            petName = "Luna",
            petType = "Gato",
            appointmentDate = "2024-02-16",
            appointmentTime = "2:00 PM",
            reason = "Revisión general",
            status = AppointmentStatus.APPROVED,
            adminNotes = "Cita confirmada para revisión general",
            createdAt = "2024-02-02",
            updatedAt = "2024-02-05"
        ),
        Appointment(
            id = "3",
            userId = "user3",
            userName = "Ana Jiménez",
            userEmail = "ana@example.com",
            petName = "Rocky",
            petType = "Perro",
            appointmentDate = "2024-02-17",
            appointmentTime = "11:30 AM",
            reason = "Problema digestivo",
            status = AppointmentStatus.PENDING,
            adminNotes = "",
            createdAt = "2024-02-03",
            updatedAt = "2024-02-03"
        ),
        Appointment(
            id = "4",
            userId = "user4",
            userName = "Luis Morales",
            userEmail = "luis@example.com",
            petName = "Mimi",
            petType = "Gato",
            appointmentDate = "2024-02-14",
            appointmentTime = "9:00 AM",
            reason = "Esterilización",
            status = AppointmentStatus.REJECTED,
            adminNotes = "No hay disponibilidad para esa fecha",
            createdAt = "2024-02-01",
            updatedAt = "2024-02-04"
        ),
        Appointment(
            id = "5",
            userId = "user5",
            userName = "Patricia Vargas",
            userEmail = "patricia@example.com",
            petName = "Buddy",
            petType = "Perro",
            appointmentDate = "2024-02-18",
            appointmentTime = "3:30 PM",
            reason = "Consulta de emergencia",
            status = AppointmentStatus.PENDING,
            adminNotes = "",
            createdAt = "2024-02-04",
            updatedAt = "2024-02-04"
        )
    )
}


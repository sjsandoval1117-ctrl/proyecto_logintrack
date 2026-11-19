const API_BASE = 'https://everyone-hiring-grant-roommate.trycloudflare.com';

document.addEventListener("DOMContentLoaded", () => {
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    if (!sessionStorage.getItem("viene_de_login")) {
        localStorage.removeItem("jwt");
        localStorage.removeItem("username");
    }

    const token = localStorage.getItem("jwt");
    const loginCardContainer = document.getElementById("login-card-container");
    const dashboardContainer = document.getElementById("dashboard-container");

    if (!token) {
        if (loginCardContainer) loginCardContainer.style.display = "flex";
        if (dashboardContainer) dashboardContainer.style.display = "none";
    } else {
        if (loginCardContainer) loginCardContainer.style.display = "none";
        if (dashboardContainer) dashboardContainer.style.display = "block";

        const username = localStorage.getItem("username") || "Sara17";
        if (document.getElementById("user-display")) {
            document.getElementById("user-display").textContent = username;
        }

        if (!sessionStorage.getItem("dashboard_limpio_hecho")) {
            sessionStorage.setItem("dashboard_limpio_hecho", "true");
            inicializarDashboardLimpio();
        } else {
            cargarDatosCompletosDashboard();
        }
    }

    const loginForm = document.getElementById("login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const usernameInput = document.getElementById("username").value.trim();
            const passwordInput = document.getElementById("password").value.trim();

            try {
                const response = await fetch(`${API_BASE}/api/auth/login`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username: usernameInput, password: passwordInput })
                });

                if (!response.ok) {
                    const errorText = await response.text().catch(() => "");
                    throw new Error(errorText || `Credenciales incorrectas (HTTP ${response.status}).`);
                }

                const data = await response.json();

                if (data.jwt) {
                    localStorage.setItem("jwt", data.jwt);
                    localStorage.setItem("username", usernameInput);
                    sessionStorage.setItem("viene_de_login", "true");
                    sessionStorage.removeItem("dashboard_limpio_hecho");
                    window.location.reload();
                } else {
                    throw new Error("El servidor no devolvió un token JWT.");
                }
            } catch (error) {
                alert(error.message);
            }
        });
    }

    const btnLogout = document.getElementById("btn-logout");
    if (btnLogout) {
        btnLogout.addEventListener("click", () => {
            localStorage.clear();
            sessionStorage.clear();
            window.location.reload();
        });
    }

    initBodegaCRUD();
    initProductoCRUD();
    initMovimientoCRUD();
});

async function authFetch(endpoint, options = {}) {
    const token = localStorage.getItem("jwt");
    const response = await fetch(`${API_BASE}${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token ? { "Authorization": `Bearer ${token}` } : {}),
            ...(options.headers || {})
        }
    });

    if (response.status === 401) {
        localStorage.clear();
        sessionStorage.clear();
        window.location.reload();
        throw new Error("Sesión expirada");
    }

    return response;
}

function inicializarDashboardLimpio() {
    document.getElementById("total-bodegas").textContent = "0";
    document.getElementById("total-productos").textContent = "0";
    document.getElementById("total-movimientos").textContent = "0";
    document.getElementById("total-auditorias").textContent = "0";

    renderTablaBodegas([]);
    renderTablaProductos([]);
    renderTablaMovimientos([]);
    renderTablaAuditorias([]);
}

async function cargarDatosCompletosDashboard() {
    try {
        const [resB, resP, resM, resA] = await Promise.all([
            authFetch("/bodegas").catch(() => ({ json: () => [] })),
            authFetch("/productos").catch(() => ({ json: () => [] })),
            authFetch("/movimientos").catch(() => ({ json: () => [] })),
            authFetch("/auditorias").catch(() => ({ json: () => [] }))
        ]);

        const bodegas = await resB.json();
        const productos = await resP.json();
        const movimientos = await resM.json();
        const auditorias = await resA.json();

        document.getElementById("total-bodegas").textContent = Array.isArray(bodegas) ? bodegas.length : 0;
        document.getElementById("total-productos").textContent = Array.isArray(productos) ? productos.length : 0;
        document.getElementById("total-movimientos").textContent = Array.isArray(movimientos) ? movimientos.length : 0;
        document.getElementById("total-auditorias").textContent = Array.isArray(auditorias) ? auditorias.length : 0;

        renderTablaBodegas(bodegas);
        renderTablaProductos(productos);
        renderTablaMovimientos(movimientos);
        renderTablaAuditorias(auditorias);
    } catch (error) {
        console.error("Error al sincronizar datos:", error);
    }
}

function renderTablaBodegas(bodegas) {
    const tbody = document.querySelector("#tabla-bodegas tbody");
    if (!tbody) return;
    tbody.innerHTML = "";
    if (!Array.isArray(bodegas) || bodegas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="2" style="padding: 0.8rem; color: #94a3b8; text-align: center;">Sin bodegas registradas.</td></tr>`;
        return;
    }
    bodegas.forEach(b => {
        const tr = document.createElement("tr");
        tr.style.borderBottom = "1px solid #f1f5f9";
        tr.innerHTML = `
            <td style="padding: 0.6rem;"><b>${b.nombre}</b><br><small style="color: #64748b;">${b.ubicacion} (Cap: ${b.capacidad})</small></td>
            <td style="padding: 0.6rem; text-align: right;">
                <button onclick="editarBodega(${b.id}, '${b.nombre}', '${b.ubicacion}', ${b.capacidad}, '${b.encargado}')" style="background: #3b82f6; color: #fff; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer; margin-right: 4px;">Editar</button>
                <button onclick="eliminarBodega(${b.id})" style="background: #ef4444; color: #fff; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer;">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function initBodegaCRUD() {
    const form = document.getElementById("bodega-form");
    if (!form) return;
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("bodega-id").value;
        const nombre = document.getElementById("nombre-bodega").value.trim();
        const ubicacion = document.getElementById("ubicacion-bodega").value.trim();
        const capacidad = parseInt(document.getElementById("capacidad-bodega").value);
        const encargado = document.getElementById("encargado-bodega").value.trim();

        const method = id ? "PUT" : "POST";
        const url = id ? `/bodegas/${id}` : "/bodegas";

        try {
            const res = await authFetch(url, {
                method,
                body: JSON.stringify({ nombre, ubicacion, capacidad, encargado })
            });

            if (!res.ok) throw new Error("No se pudo guardar la bodega.");

            form.reset();
            document.getElementById("bodega-id").value = "";
            document.getElementById("btn-guardar-bodega").textContent = "Guardar Bodega";
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Error en Bodega:", err);
            alert("Error al guardar la bodega.");
        }
    });
}

window.editarBodega = (id, nombre, ubicacion, capacidad, encargado) => {
    document.getElementById("bodega-id").value = id;
    document.getElementById("nombre-bodega").value = nombre;
    document.getElementById("ubicacion-bodega").value = ubicacion;
    document.getElementById("capacidad-bodega").value = capacidad;
    document.getElementById("encargado-bodega").value = encargado;
    document.getElementById("btn-guardar-bodega").textContent = "Actualizar Bodega";
};

window.eliminarBodega = async (id) => {
    if (confirm("¿Estás segura de eliminar esta bodega?")) {
        try {
            const res = await authFetch(`/bodegas/${id}`, { method: "DELETE" });
            if (res.status === 403) {
                alert("Acceso denegado (403): Tu usuario no tiene permisos para eliminar este registro.");
                return;
            }
            if (!res.ok) throw new Error("No se pudo eliminar la bodega.");
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Excepción al eliminar bodega:", err);
            alert("Error al intentar eliminar la bodega.");
        }
    }
};

function renderTablaProductos(productos) {
    const tbody = document.querySelector("#tabla-productos tbody");
    if (!tbody) return;
    tbody.innerHTML = "";
    if (!Array.isArray(productos) || productos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" style="padding: 0.8rem; color: #94a3b8; text-align: center;">Sin productos registrados.</td></tr>`;
        return;
    }
    productos.forEach(p => {
        const tr = document.createElement("tr");
        tr.style.borderBottom = "1px solid #f1f5f9";
        tr.innerHTML = `
            <td style="padding: 0.6rem;"><b>${p.nombre}</b><br><small style="color: #64748b;">${p.categoria}</small></td>
            <td style="padding: 0.6rem;">Stock: ${p.stock}<br>$${p.precio}</td>
            <td style="padding: 0.6rem; text-align: right;">
                <button onclick="editarProducto(${p.id}, '${p.nombre}', '${p.categoria}', ${p.stock}, ${p.precio})" style="background: #3b82f6; color: #fff; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer; margin-right: 4px;">Editar</button>
                <button onclick="eliminarProducto(${p.id})" style="background: #ef4444; color: #fff; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer;">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function initProductoCRUD() {
    const form = document.getElementById("producto-form");
    if (!form) return;
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("producto-id").value;
        const nombre = document.getElementById("nombre-producto").value.trim();
        const categoria = document.getElementById("categoria-producto").value.trim();
        const stock = parseInt(document.getElementById("stock-producto").value);
        const precio = parseFloat(document.getElementById("precio-producto").value);

        const method = id ? "PUT" : "POST";
        const url = id ? `/productos/${id}` : "/productos";

        try {
            const res = await authFetch(url, {
                method,
                body: JSON.stringify({ nombre, categoria, stock, precio })
            });

            if (!res.ok) throw new Error("No se pudo guardar el producto.");

            form.reset();
            document.getElementById("producto-id").value = "";
            document.getElementById("btn-guardar-producto").textContent = "Guardar Producto";
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Error en Producto:", err);
            alert("Error al guardar el producto.");
        }
    });
}

window.editarProducto = (id, nombre, categoria, stock, precio) => {
    document.getElementById("producto-id").value = id;
    document.getElementById("nombre-producto").value = nombre;
    document.getElementById("categoria-producto").value = categoria;
    document.getElementById("stock-producto").value = stock;
    document.getElementById("precio-producto").value = precio;
    document.getElementById("btn-guardar-producto").textContent = "Actualizar Producto";
};

window.eliminarProducto = async (id) => {
    if (confirm("¿Estás segura de eliminar este producto?")) {
        try {
            const res = await authFetch(`/productos/${id}`, { method: "DELETE" });
            if (res.status === 403) {
                alert("Acceso denegado (403): Tu usuario no tiene permisos para eliminar este registro.");
                return;
            }
            if (!res.ok) throw new Error("No se pudo eliminar el producto.");
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Excepción al eliminar producto:", err);
            alert("Error al intentar eliminar el producto.");
        }
    }
};

function renderTablaMovimientos(movimientos) {
    const tbody = document.querySelector("#tabla-movimientos tbody");
    if (!tbody) return;
    tbody.innerHTML = "";
    if (!Array.isArray(movimientos) || movimientos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" style="padding: 0.8rem; color: #94a3b8; text-align: center;">Sin movimientos registrados.</td></tr>`;
        return;
    }
    movimientos.forEach(m => {
        const tr = document.createElement("tr");
        tr.style.borderBottom = "1px solid #f1f5f9";
        tr.innerHTML = `
            <td style="padding: 0.6rem;"><b>${m.tipoMovimiento}</b><br><small style="color: #64748b;">Destino: ${m.bodegaDestino}</small></td>
            <td style="padding: 0.6rem;">${m.cantidad}</td>
            <td style="padding: 0.6rem; text-align: right;">
                <button onclick="eliminarMovimiento(${m.id})" style="background: #ef4444; color: #fff; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer;">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function initMovimientoCRUD() {
    const form = document.getElementById("movimiento-form");
    if (!form) return;
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const tipoMovimiento = document.getElementById("tipo-movimiento").value;
        const bodegaDestino = document.getElementById("bodega-destino").value.trim();
        const bodegaOrigen = document.getElementById("bodega-origen").value.trim();
        const cantidad = parseInt(document.getElementById("cantidad-movimiento").value);
        const productoId = parseInt(document.getElementById("producto-id").value);

        const movimientoData = {
            tipoMovimiento,
            bodegaDestino,
            bodegaOrigen: bodegaOrigen || null,
            cantidad,
            productoId,
            usuarioResponsable: localStorage.getItem("username") || "Sara17"
        };

        try {
            const res = await authFetch("/movimientos", {
                method: "POST",
                body: JSON.stringify(movimientoData)
            });

            if (!res.ok) throw new Error("No se pudo registrar el movimiento.");

            form.reset();
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Error en Movimiento:", err);
            alert("Error al registrar el movimiento.");
        }
    });
}

window.eliminarMovimiento = async (id) => {
    if (confirm("¿Eliminar este movimiento?")) {
        try {
            const res = await authFetch(`/movimientos/${id}`, { method: "DELETE" });
            if (res.status === 403) {
                alert("Acceso denegado (403): Tu usuario no tiene permisos para eliminar este registro.");
                return;
            }
            if (!res.ok) throw new Error("No se pudo eliminar el movimiento.");
            cargarDatosCompletosDashboard();
        } catch (err) {
            console.error("Excepción al eliminar movimiento:", err);
            alert("Error al intentar eliminar el movimiento.");
        }
    }
};

function renderTablaAuditorias(auditorias) {
    const tbody = document.querySelector("#tabla-auditorias tbody");
    if (!tbody) return;
    tbody.innerHTML = "";
    if (!Array.isArray(auditorias) || auditorias.length === 0) {
        tbody.innerHTML = `<tr><td style="padding: 0.8rem; color: #94a3b8; text-align: center;">Sin auditorías registradas.</td></tr>`;
        return;
    }
    auditorias.forEach(a => {
        const tr = document.createElement("tr");
        tr.style.borderBottom = "1px solid #f1f5f9";
        tr.innerHTML = `
            <td style="padding: 0.6rem; color: #334155;">
                <b>[${a.tipoOperacion}]</b> ${a.entidadAfectada} (${a.usuarioResponsable})<br>
                <small style="color: #64748b;">${new Date(a.fechaHora).toLocaleString()}</small>
            </td>
        `;
        tbody.appendChild(tr);
    });
}
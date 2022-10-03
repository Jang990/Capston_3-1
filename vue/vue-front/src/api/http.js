import axios from "axios"
import store from "@/store/store";
const instance = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        "Content-Type": "application/json",
        // Authorization: `Bearer ${this.$store.auth.token}`
        Authorization: `Bearer test`
    }
});


export default instance;
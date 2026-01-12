# Basis-Image
FROM node:22 AS build

# Arbeitsverzeichnis setzen
WORKDIR /app

# Abhängigkeiten kopieren und installieren
COPY package.json package-lock.json ./
RUN npm install

# Projektdateien kopieren
COPY . .

# Build erstellen
RUN npm run build

# Produktions-Image
FROM nginx:stable-alpine

# Build-Output kopieren
COPY --from=build /app/dist /usr/share/nginx/html 



# Eigene Nginx-Konfiguration
COPY nginx.conf /etc/nginx/conf.d/default.conf

  
# Exponiere Port 80
EXPOSE 80

# Nginx starten
CMD ["nginx", "-g", "daemon off;"]
# Use the official Node.js image as the base image
FROM node:16.14 as build-stage

# Create a directory for the app inside the container
WORKDIR /app/client

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install Angular CLI and project dependencies
RUN npm install -g @angular/cli@latest && npm install

# Copy all frontend source code to the container
COPY . .

EXPOSE 80

CMD ["npm", "run", "start_docker"]

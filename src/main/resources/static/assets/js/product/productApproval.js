document.addEventListener("DOMContentLoaded", function() {
  const imageInput = document.getElementById("proImage");
  const imagePreview = document.getElementById("image-preview");
  const imageUploadLabel = document.querySelector('.image-upload label');
  const imageUpload = document.querySelector('.image-upload');
  

  imageInput.addEventListener("change", handleFiles);
  

  imageUpload.addEventListener("dragover", function(event) {
      event.preventDefault();
      imageUpload.classList.add("dragover");
  });

  imageUpload.addEventListener("dragleave", function() {
      imageUpload.classList.remove("dragover");
  });

  imageUpload.addEventListener("drop", function(event) {
      event.preventDefault();
      imageUpload.classList.remove("dragover");
      const files = event.dataTransfer.files;
      handleFiles({ target: { files: files } });
  });

  function handleFiles(event) {
      const files = event.target.files;
      imagePreview.innerHTML = "";
      
      for (const file of files) {
          const reader = new FileReader();
          
          reader.onload = function(e) {
              const imgElement = document.createElement("img");
              imgElement.src = e.target.result;
              imagePreview.appendChild(imgElement);
          };

          reader.readAsDataURL(file);
      }



      if (files.length > 0) {
          imageUploadLabel.style.display = 'none';
      } else {
          imageUploadLabel.style.display = 'flex';
      }
  }
});

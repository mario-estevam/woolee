const curtirPost = async (id) =>{
   const quantidadeCurtidas = document.querySelector(`#post-${id} .quantidade-curtidas`);
   const iconeCurtidas = document.querySelector(`#post-${id} .icone-curtida`);
   const response = await fetch(`/curtir/post/${id}`);

   response.text().then((value) => {

      iconeCurtidas.classList.toggle('text-danger');
      quantidadeCurtidas.innerHTML = `${value} curtidas`;
   });}

function openModalExcluir(id) {
   const hrefItem = document.querySelector('.href-item');

   $('#exampleModal').modal('show')
   hrefItem.setAttribute('href', `/deletar/comentario/${id}`);

}
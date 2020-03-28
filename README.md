Demonstrates infinite looping bug when an IllegalStateException is thrown in ParticleManager::loadTextureLists

   private void loadTextureLists(IResourceManager manager, ResourceLocation particleId, Map<ResourceLocation, List<ResourceLocation>> textures) {
      ResourceLocation resourcelocation = new ResourceLocation(particleId.getNamespace(), "particles/" + particleId.getPath() + ".json");

      try (
         IResource iresource = manager.getResource(resourcelocation);
         Reader reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
      ) {
         TexturesParticle texturesparticle = TexturesParticle.deserialize(JSONUtils.fromJson(reader));
         List<ResourceLocation> list = texturesparticle.getTextures();
         boolean flag = this.sprites.containsKey(particleId);
         if (list == null) {
            if (flag) {
               throw new IllegalStateException("Missing texture list for particle " + particleId);
            }
         } else {
            if (!flag) {
               throw new IllegalStateException("Redundant texture list for particle " + particleId);            <-------- here
            }

            textures.put(particleId, list.stream().map((p_228349_0_) -> {
               return new ResourceLocation(p_228349_0_.getNamespace(), "particle/" + p_228349_0_.getPath());
            }).collect(Collectors.toList()));
         }

      } catch (IOException ioexception) {
         throw new IllegalStateException("Failed to load description for particle " + particleId, ioexception);
      }
   } 
   
 The error can be triggered by the code in StartupClientOnly::onParticleFactoryRegistration,
 by using the "wrong" ParticleManager::registerFactory() method
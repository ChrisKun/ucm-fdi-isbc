class Partido
  attr_accessor :equipoLocal, :equipoVisitante, :resEqL, :resEqV
  
  def initialize(eL, rEL, eV, rEV)
    @equipoLocal = eL ; @resEqL = rEL
    @equipoVisitante = eV ; @resEqV = rEV
  end
  
  def to_s
    return @equipoLocal +' '+ @resEqL +' '+ @resEqV +' '+ @equipoVisitante
  end
  
  def to_file
    return @equipoLocal +','+ @resEqL +','+ @equipoVisitante +','+ @resEqV
  end
end


class Clasificacion
  attr_accessor :eq
  def initialize(pos,eq,pj,pg,pe,pp,gf,gc,nsnc)
    @pos = pos ; @eq = eq
    @pj = pj ; @pg = pg ; @pe = pe ; @pp = pp
    @gf = gf ; @gc = gc
    @nsnc = nsnc
  end
  
  def to_s
    return @pos+' '+@eq+' '+@pj+' '+@pg+' '+@pe+' '+@pp+' '+@gf+' '+@gc+' '+@nsnc
  end
  
  def to_file
    return @pos+','+@eq+','+@pj+','+@pg+','+@pe+','+@pp+','+@gf+','+@gc+','+@nsnc
  end
  
end

class Jornada
  # Partido[] partidos, Clasificacion[] clas
  def initialize(partidos, clas)
    @partidos = partidos
    @clas = clas
  end
  
  def to_file
    # 0. Con el array de los resultados de los partidos y la clasificacion
    # 1. Coger un partido e identificar los equipos
    # 2. Buscar las clasificaciones de los equipos
    # 3. Añadir al resultado las clasificaciones de local y visitante
    # 4. Ir a 1 y repetir mientras haya partidos
    
    temp_s = ''
    @partidos.each do |part|
      temp_s = temp_s + part.to_file
      @clas.each do |c|
        if ( c.eq == part.equipoLocal ) then
          temp_s = temp_s +','+ c.to_file
        end
      end
      
      @clas.each do |c|
        if ( c.eq == part.equipoVisitante ) then
          temp_s = temp_s +','+ c.to_file
        end
      end
      
      temp_s = temp_s + "\n"

    end
    return temp_s
  end
end

=begin
partidos = []
partidos.push(Partido.new('Ponferradina','5','Getafe','0'))
partidos.push(Partido.new('Barcelona','3','Madrid','2'))
clasificaciones = []
clasificaciones.push(Clasificacion.new('23','Getafe','34','23','1','12','11','41','100'))
clasificaciones.push(Clasificacion.new('4','Ponferradina','34','35','0','1','55','24','70'))
clasificaciones.push(Clasificacion.new('2','Madrid','36','30','12','126','11','41','100'))
clasificaciones.push(Clasificacion.new('1','Barcelona','34','31','15','120','11','41','100'))

jornada = Jornada.new(partidos,clasificaciones)
puts jornada.to_file
=end